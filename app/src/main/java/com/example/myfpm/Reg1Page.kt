package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reg1_page.*

class Reg1Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg1_page)

        next_button1.setOnClickListener {
            if(!checkData()) return@setOnClickListener

            Log.d("Reg1Page", "!!!Start RegPersonPage!!!")
            val intent = Intent(this, RegPersonPage::class.java )
            startActivity(intent)
        }
    }

    private fun checkData(): Boolean{
        val email: String = email_reg_edit_text.text.toString()
        val password: String = password_edit_text.text.toString()
        var isDataCorrect: Boolean = true

        if(email.isEmpty()) {
            email_reg_edit_text.error = "Please enter login"
            email_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("Reg1Page", "!!!Empty email!!!")
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_reg_edit_text.error = "Please enter valid login"
            email_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("Reg1Page", "!!!Invalid email!!!")
        }

        if(password.isEmpty()) {
            password_edit_text.error = "Please enter password"
            password_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("Reg1Page", "!!!Empty password!!!")
        }


        Log.d("Reg1Page", "!!!Email: $email!!!")
        Log.d("Reg1Page", "!!!Password: $password!!!")

        return isDataCorrect
    }
}
