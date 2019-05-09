package com.example.myfpm

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        val password = intent.getStringExtra("password")
        val photo = intent.getParcelableExtra<Uri>("photo")


        if(!checkData()) return

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful){
                    Log.d("Reg2Page", "!!!${it.exception}!!!")
                    return@addOnCompleteListener
                }
                else {
                    continueRegistration(it, name, surname, phone, email, photo)
                }
            }
    }

    private fun continueRegistration(it: Task<AuthResult>, name: String, surname: String,
                                     phone: String, email: String, photo: Uri){

        val uid = it.result!!.user.uid

        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("EmailVer", "!!!Email did not send!!!")
                    Toast.makeText(this, "${it.exception}\nEmail did not sent",
                        Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                else {
                    Log.d("EmailVer", "!!!Email sent!!!")
                    Log.d(
                        "Reg2Page", "!!!" +
                                "Registration successful\n" +
                                "User uid: $uid" +
                                "!!!"
                    )
                    syncDB(name, surname, phone, email, uid, photo)
                }
            }

    }

    private fun syncDB(name: String, surname: String, phone: String,
                       email: String, uid: String, photo: Uri?){

        val year = year_reg_spinner.selectedItem.toString()
        val spec = spec_reg_spinner.selectedItem.toString()
        val group = group_reg_spinner.selectedItem.toString()
        val photoFile = UUID.randomUUID().toString()

        val studentData = HashMap<String, Any>()
        studentData["name"] = name
        studentData["surname"] = surname
        studentData["year"] = year
        studentData["spec"] = spec
        studentData["group"] = group
        studentData["role"] = "student"
        studentData["email"] = email

        if(phone.isNotEmpty()){
            studentData["phone"] = phone
        }
        else
        {
            studentData["phone"] = "null"
        }
        if(photo != null) {
            FirebaseStorage.getInstance().getReference("/images/$photoFile")
                .putFile(photo)
            studentData["photoUrl"] = photoFile
        }
        else
            studentData["photoUrl"] = "null"

        FirebaseFirestore.getInstance()
            .collection("students")
            .document(uid).set(studentData)
            .addOnSuccessListener {

                Log.d("Reg2Page/syncBD", "!!!Sync studentData is successful!!!")

                val intent = Intent(this, MainActivity::class.java )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }.addOnFailureListener {
                Toast.makeText(this, " ${it.message}\nRegistration failed",
                    Toast.LENGTH_SHORT).show()
                Log.d("Reg2Page/syncBD",
                "!!!Sync studentData failed: ${it.message}!!!")
            }
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
