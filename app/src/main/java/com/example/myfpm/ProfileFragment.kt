package com.example.myfpm


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private lateinit var fio: TextView
    private lateinit var group: TextView
    private lateinit var phone: TextView
    private lateinit var spec: TextView
    private lateinit var profileImage: ImageView
    private lateinit var profileLayout: ConstraintLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        fio = prof_name
        group = prof_group
        phone = prof_telNum
        spec = prof_spec
        profileImage = prof_image
        profileLayout = profile_layout

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

                    Picasso.get().load(student.imageUrl).centerCrop()
                        .resize(400, 400).into(profileImage)

                    val fioStr = "${student.name} ${student.surname}"

                    fio.text = "Ф.И.О: " + fioStr
                    group.text = "Группа: " + student.group
                    spec.text =  "Специальность: " + student.spec
                    phone.text = "Моб. телефон: " + student.phone

                    profileLayout.visibility = View.VISIBLE
                }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.singOut_butt -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this.context,LoginActivity::class.java)
                intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
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

class Student(val email: String, val  group: String, val imageUrl: String, val name: String,
              val phone: String, val spec: String, val surname: String, val year: String){
    constructor():this("", "", "", "", "",
        "", "", "")
}
