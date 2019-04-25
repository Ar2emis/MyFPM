package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        btn_sign_in.setOnClickListener {
            sign_in()
        }
    }

    private fun sign_in() {
        if(login_text.text.toString().isEmpty()) {
            login_text.error = "Please enter login"
            login_text.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(login_text.text.toString()).matches()) {
            login_text.error = "Please enter valid login"
            login_text.requestFocus()
            return
        }

        if(password_text.text.toString().isEmpty()) {
            login_text.error = "Please enter password"
            login_text.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(login_text.text.toString(), password_text.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.\n Wrong login or password",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            val intent = Intent(this, MyFPMpage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        else{
            password_text.text = null
        }
    }

    fun onClick(view: View) {
        val intent = Intent(this, Reg1Page::class.java)
        startActivity(intent)
    }
}
