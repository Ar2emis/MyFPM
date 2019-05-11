package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_in_login_button.setOnClickListener {
            val email: String = email_forg_pass_edit_text.text.toString()
            val password: String = password_login_edit_text.text.toString()

            if(!checkData(email, password)){
                Log.d("LoginPage", "!!!Invalid email or password!!!")
                return@setOnClickListener
            }
            else {
                sign_in(email, password)
            }
        }

        regStart_login_text_view.setOnClickListener {
            Log.d("LoginPage", "!!!Start registration!!!")
            val intent = Intent(this, Reg1Page::class.java)
            startActivity(intent)
        }

        forgot_password_text_view.setOnClickListener {
            Log.d("LoginPage", "!!!Start resetting password!!!")
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkData(email: String, password: String): Boolean{
        var isDataCorrect = true

        if(email.isEmpty()) {
            email_forg_pass_edit_text.error = "Please enter login"
            email_forg_pass_edit_text.requestFocus()
            isDataCorrect = false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_forg_pass_edit_text.error = "Please enter valid login"
            email_forg_pass_edit_text.requestFocus()
            isDataCorrect = false
        }

        if(password.isEmpty()) {
            password_login_edit_text.error = "Please enter password"
            password_login_edit_text.requestFocus()
            isDataCorrect = false
        }

        return isDataCorrect
    }

    private fun sign_in(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if(!FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                        email_forg_pass_edit_text.error = "Email isn't verified"
                        email_forg_pass_edit_text.requestFocus()
                    }
                    else {
                        val intent = Intent(this, MyFPMpage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed. Wrong email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                    password_login_edit_text.text = null
                }
            }
    }
}
