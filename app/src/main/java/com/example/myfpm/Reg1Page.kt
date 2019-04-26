package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_reg1_page.*

class Reg1Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg1_page)

        next_button1.setOnClickListener {

            var isDataIncorrect: Boolean = false

            if(email_reg_edit_text.text.toString().isEmpty()) {
                email_reg_edit_text.error = "Please enter login"
                email_reg_edit_text.requestFocus()
                isDataIncorrect = true
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email_reg_edit_text.text.toString()).matches()) {
                email_reg_edit_text.error = "Please enter valid login"
                email_reg_edit_text.requestFocus()
                isDataIncorrect = true
            }

            if(password_edit_text.text.toString().isEmpty()) {
                password_edit_text.error = "Please enter password"
                password_edit_text.requestFocus()
                isDataIncorrect = true

            }

            if(isDataIncorrect) return@setOnClickListener

            val intent = Intent(this, RegPersonPage::class.java )
            startActivity(intent)
        }
    }
}
