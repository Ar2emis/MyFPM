package com.example.myfpm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_reg2_page.*
import java.util.*
import kotlin.collections.HashMap

class Reg2Page : AppCompatActivity() {

    var years: MutableList<String> = mutableListOf("Year")
    var specs: MutableList<String> = mutableListOf("Spec")
    var groups: MutableList<String> = mutableListOf("Group")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg2_page)

        FirebaseFirestore.getInstance().collection("reg_spinners").get()
            .addOnSuccessListener { initializeStartSpinner(it, years, year_reg_spinner) }

        sign_up_reg_button.setOnClickListener { signUp() }

        year_reg_spinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    spec_reg_text_view.visibility = View.INVISIBLE
                    spec_reg_spinner.visibility = View.INVISIBLE
                    group_reg_text_view.visibility = View.INVISIBLE
                    group_reg_spinner.visibility = View.INVISIBLE
                }
                else {
                    spec_reg_text_view.visibility = View.VISIBLE
                    spec_reg_spinner.visibility = View.VISIBLE

                    val year = year_reg_spinner.getItemAtPosition(position).toString()
                    Log.d("Reg2Page",
                        "!!!$year!!!")
                    FirebaseFirestore.getInstance()
                        .collection("reg_spinners/year_$year/specs").get()
                        .addOnSuccessListener { initializeStartSpinner(it, specs, spec_reg_spinner) }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spec_reg_spinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    group_reg_text_view.visibility = View.INVISIBLE
                    group_reg_spinner.visibility = View.INVISIBLE
                }
                else {
                    group_reg_text_view.visibility = View.VISIBLE
                    group_reg_spinner.visibility = View.VISIBLE

                    val year = year_reg_spinner.selectedItem.toString()
                    val spec = spec_reg_spinner.selectedItem.toString()
                        .substringAfter('(').substringBefore(')')
                    Log.d("Reg2Page",
                        "!!!$year!!!")
                    FirebaseFirestore.getInstance()
                        .collection("reg_spinners/year_$year/specs/spec_$spec/groups").get()
                        .addOnSuccessListener { initializeStartSpinner(it, groups, group_reg_spinner) }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initializeStartSpinner(it: QuerySnapshot,
                                       list: MutableList<String>, spinner: Spinner){
        val item = list[0]
        list.clear()
        list.add(item)

        if(it.isEmpty) return

        for(doc in it) {
            list.add(doc.getString("name")!!)
            Log.d("Spinner", "\t!!!${doc.getString("name")}!!!")
        }

        spinner.adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, list)
    }

    private fun signUp(){
        val name: String = intent.getStringExtra("name")
        val surname: String = intent.getStringExtra("surname")
        val phone: String = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val password= intent.getStringExtra("password")
        val imageUri = intent.getParcelableExtra<Uri>("imageUri")


        if(!checkData()) return

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful){
                    Log.d("Reg2Page", "!!!${it.exception}!!!")
                    return@addOnCompleteListener
                }
                else
                    Log.d("Reg2Page", "!!!" +
                            "Registration successful\n" +
                            "User uid: ${it.result?.user?.uid}" +
                            "!!!")
                syncDB(name, surname, phone, email, imageUri, it.result?.user?.uid!!)
            }
    }

    private fun syncDB(name: String, surname: String, phone: String,
                       email: String, imageUri: Uri, uid: String){
        val year = year_reg_spinner.selectedItem.toString()
        val spec = spec_reg_spinner.selectedItem.toString()
        val group = group_reg_spinner.selectedItem.toString()
        val imageFileName = UUID.randomUUID().toString()

        val studentData = HashMap<String, Any>()
        studentData["name"] = name
        studentData["surname"] = surname
        studentData["phone"] = phone
        studentData["email"] = email
        studentData["year"] = year
        studentData["spec"] = spec
        studentData["group"] = group

        val ref = FirebaseStorage.getInstance().getReference("images/$imageFileName")
        ref.putFile(imageUri)
        studentData["imageUrl"] = ref.toString()

        FirebaseFirestore.getInstance()
            .collection("students")
            .document(uid).set(studentData)
            .addOnSuccessListener {

                Log.d("Reg2Page/syncBD", "!!!Sync studentData is successful!!!")

                val intent = Intent(this, MyFPMpage::class.java )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }.addOnFailureListener { Log.d("Reg2Page/syncBD",
                "!!!Sync studentData failed: ${it.message}!!!") }
    }

    private fun checkData(): Boolean{

        if(year_reg_spinner.selectedItem == null ||
            year_reg_spinner.selectedItem.toString() == years[0]){
            Toast.makeText(this, "You have to choose year", Toast.LENGTH_LONG).show()
            return false
        }

        if(spec_reg_spinner.selectedItem == null ||
            spec_reg_spinner.selectedItem.toString() == specs[0]){
            Toast.makeText(this, "You have to choose spec", Toast.LENGTH_LONG).show()
            return false
        }

        if(group_reg_spinner.selectedItem == null ||
            group_reg_spinner.selectedItem.toString() == groups[0]){
            Toast.makeText(this, "You have to choose group", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}