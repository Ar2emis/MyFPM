package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import kotlinx.android.synthetic.main.activity_reg1_page.*
import kotlinx.android.synthetic.main.activity_reg_person_page.*

class RegPersonPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_person_page)

        next_button2.setOnClickListener {

            var isDataIncorrect: Boolean = false

            if (name_reg_edit_text.text.toString().isEmpty()) {
                name_reg_edit_text.error = "Please enter name"
                name_reg_edit_text.requestFocus()
                isDataIncorrect = true
            }

            if (surname_reg_edit_text.text.toString().isEmpty()) {
                surname_reg_edit_text.error = "Please enter surname"
                surname_reg_edit_text.requestFocus()
                isDataIncorrect = true
            }

            if (!Patterns.PHONE.matcher(phone_reg_edit_text.text.toString()).matches()) {
                phone_reg_edit_text.error = "Please enter valid phone number"
                phone_reg_edit_text.requestFocus()
                isDataIncorrect = true
            }

            if (phone_reg_edit_text.text.toString().isEmpty()) {
                phone_reg_edit_text.error = "Please enter phone number"
                phone_reg_edit_text.requestFocus()
                isDataIncorrect = true
            }

            if (isDataIncorrect) return@setOnClickListener

            val intent = Intent(this, Reg2Page::class.java)
            startActivity(intent)
        }
    }
}
