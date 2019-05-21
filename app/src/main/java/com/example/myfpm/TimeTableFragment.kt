package com.example.myfpm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.day_timetable.view.*
import kotlinx.android.synthetic.main.fragment_time_table.*

class TimeTableFragment : androidx.fragment.app.Fragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_time_table, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        recyclerView = rec_view_time_table

        FirebaseAuth.getInstance().addAuthStateListener {
            val userUid = it.currentUser?.uid
            Log.d("Profile", "!!! $userUid !!!")

            if(userUid.isNullOrEmpty()) {
                Log.d("Profile", "Invalid user")
                return@addAuthStateListener
            }

            FirebaseFirestore.getInstance().collection("students")
                .document(userUid).get().addOnCompleteListener {

                    if(!it.isSuccessful || it.result == null){
                        Toast.makeText(this.context, "Connection error",
                            Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }

                    val student = it.result!!.toObject(Student::class.java)?:
                    return@addOnCompleteListener

                    fetchTimeTable(student)
                }
        }

    }

    private fun fetchTimeTable(student: Student){

        val specName = student.spec.substringAfter("(").substringBefore(")")
        val groupName = student.group.substringAfter("-")

        FirebaseFirestore.getInstance()
            .collection("specs/spec_$specName/groups/group_$groupName/timetable")
            .orderBy("id", Query.Direction.ASCENDING).get()
            .addOnCompleteListener {
                if(!it.isSuccessful || it.result == null){
                    Toast.makeText(this.context, "Connection error", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                val days = it.result!!.toObjects(Day::class.java)

                val adapter = GroupAdapter<ViewHolder>()

                for(day in days) {
                    adapter.add(DayItem(day))
                    Log.d("TimeTable", "!!! ${day.name} !!!")
                }

                recyclerView.adapter = adapter
            }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.singOut_butt -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this.context,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.profmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

class DayItem(private val day: Day): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.day_timetable
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.name_of_a_day.text = day.name

        var pairListStr: String = ""
        var i = 0
        for(pair in day.lessonsList) {
            if(pair.contains("/"))
                pairListStr += pair.substringBefore("/") + "\n" +
                    pair.substringAfter("/")
            else
                pairListStr += pair

            if(i != day.lessonsList.size - 1) {
                pairListStr += "\n\n"
                ++i
            }
            else
                pairListStr += "\n"
        }

        viewHolder.itemView.day_lessons.text = pairListStr

        viewHolder.itemView.visibility = View.VISIBLE
    }


}

class Day(val id: Int, val name: String, val lessonsList: List<String>){
    constructor():this(0,"", listOf<String>())
}
