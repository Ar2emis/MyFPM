package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reg2_page.*

class Reg2Page : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg2_page)

        sign_up_reg_button.setOnClickListener {
            val name: String = intent.getStringExtra("name")
            val surname: String = intent.getStringExtra("surname")
            val phone: String = intent.getStringExtra("phone")
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")

            if(!checkData()) return@setOnClickListener

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
                    syncDB(name, surname, phone, email, password, it.result!!.user.uid)
                }

            val intent = Intent(this, MyFPMpage::class.java )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun syncDB(name: String, surname: String, phone: String,
                       email: String, password: String, uid: String){
        val coll = FirebaseFirestore.getInstance()
            .collection("users")

        val dataToSave = HashMap<String, Any>()
        dataToSave["email"] = email
        dataToSave["password"] = password
        dataToSave["uid"] = uid

        coll.document(uid).set(dataToSave).addOnSuccessListener {
            Log.d("Reg2Page/syncBD", "!!!Sync DB is successful!!!")
        }.addOnFailureListener {
            Log.d("Reg2Page/syncBD", "!!!Sync DB failed: ${it.message}!!!")
        }
    }

    private fun checkData(): Boolean{
        var isDataCorrect = true


        return isDataCorrect
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
