package com.example.myfpm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import kotlinx.android.synthetic.main.activity_reg1_page.*
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.action_bar)

        reset_password_forg_pass_button.setOnClickListener {
            val email = email_forg_pass_edit_text.text.toString().trim()

            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnSuccessListener {
                    checkUser(it ,email)
                }
        }
    }

    private fun checkUser(it: SignInMethodQueryResult, email: String){
        if(it.signInMethods.isNullOrEmpty()){
            Log.d("ResetPasswordPage", "!!!Email not already in use: $email!!!")
            email_forg_pass_edit_text.error = "Email not already in use"
            email_forg_pass_edit_text.requestFocus()
            return
        }
        else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if(!it.isSuccessful){
                        Toast.makeText(this, "${it.exception}\nResetting email did not send",
                            Toast.LENGTH_SHORT).show()
                        Log.d("ResetPassword", "!!!Resetting email did not send!!!")
                        return@addOnCompleteListener
                    }
                    else {
                        Toast.makeText(this, "Resetting email sent",
                            Toast.LENGTH_SHORT).show()
                        Log.d("ResetPassword", "!!!Resetting email sent!!!")
                    }
                }
        }
    }
}
