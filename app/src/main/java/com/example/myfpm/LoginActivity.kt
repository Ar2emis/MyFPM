package com.example.myfpm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.action_bar)

        checkAndAskPermissions()

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

    private fun checkAndAskPermissions(){
        val PERMISSION_ALL = 0
        val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        if(!hasPermissions(this, PERMISSIONS))
        { ActivityCompat.requestPermissions(this,
            PERMISSIONS,
            PERMISSION_ALL)
        }
    }

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for(permission in permissions)
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
        }
        return true
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
