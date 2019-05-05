package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reg1_page.*

class Reg1Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg1_page)

        next_button1.setOnClickListener {
            val email: String = email_reg_edit_text.text.toString().trim()
            val password: String = password_reg_edit_text.text.toString()

            if(!checkData(email, password)){
                Log.d("Reg1Page", "!!!Invalid data!!!")
                return@setOnClickListener
            }
            else
            {
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnSuccessListener {
                        checkUser(it ,email, password)
                    }
            }
        }
    }

    private fun checkUser(it: SignInMethodQueryResult, email: String, password: String){
        if(!it.signInMethods.isNullOrEmpty()){
            Log.d("Reg1Page/DB", "!!!Email already in use: $email!!!")
            email_reg_edit_text.error = "Email already in use"
            email_reg_edit_text.requestFocus()
            return
        }
        else{
            Log.d("Reg1Page", "!!!\\nEmail: $email\nPassword: $password\n!!!")

            Log.d("Reg1Page", "!!!Start RegPersonPage!!!")
            val intent = Intent(this, RegPersonPage::class.java )

            intent.putExtra("email", email)
            intent.putExtra("password", password)

            startActivity(intent)
        }
    }

    private fun checkData(email: String, password: String): Boolean{
        var isDataCorrect: Boolean = true
        val PASSWORD_LENGTH = 8

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_reg_edit_text.error = "Please enter valid login"
            email_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("Reg1Page", "!!!Invalid email!!!")
        }

        if(email.isEmpty()) {
            email_reg_edit_text.error = "Please enter login"
            email_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("Reg1Page", "!!!Empty email!!!")
        }

        if(password.isEmpty()) {
            password_reg_edit_text.error = "Please enter password"
            password_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("Reg1Page", "!!!Empty password!!!")
        }
        else{
            if(password.length < PASSWORD_LENGTH){
                password_reg_edit_text.error = "Invalid password"
                password_reg_edit_text.requestFocus()
                isDataCorrect = false
                Log.d("Reg1Page", "!!!Invalid password!!!")
            }
        }

        Log.d("Reg1Page", "!!!Email: $email!!!")
        Log.d("Reg1Page", "!!!Password: $password!!!")

        return isDataCorrect
    }
}
