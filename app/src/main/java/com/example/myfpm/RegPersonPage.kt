package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_reg_person_page.*

class RegPersonPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_person_page)

        next_button2.setOnClickListener {
            val name: String = name_reg_edit_text.text.toString()
            val surname: String = surname_reg_edit_text.text.toString()
            val phone: String = phone_reg_edit_text.text.toString()
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")

            if(!checkData(name, surname, phone)) {
                Log.d("RegPersonPage", "!!!Invalid data!!!")
                return@setOnClickListener
            }
            else
                Log.d("RegPersonPage",
                    "!!!\nName: $name\nSurname: $surname\nPhone: $phone\n!!!")

            Log.d("RegPersonPage", "!!!Start Reg2Page!!!")
            val intent = Intent(this, Reg2Page::class.java)

            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("name", name)
            intent.putExtra("surname", surname)
            intent.putExtra("phone", phone)

            startActivity(intent)
        }
    }

    private fun checkData(name: String, surname: String, phone: String): Boolean{
        var isDataCorrect: Boolean = true

        if (name.isEmpty()) {
            name_reg_edit_text.error = "Please enter name"
            name_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Empty name!!!")
        }

        if (surname.isEmpty()) {
            surname_reg_edit_text.error = "Please enter surname"
            surname_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Empty surname!!!")
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            phone_reg_edit_text.error = "Please enter valid phone number"
            phone_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Invalid phone number!!!")
        }

        if (phone.isEmpty()) {
            phone_reg_edit_text.error = "Please enter phone number"
            phone_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Empty phone number!!!")
        }

        return isDataCorrect
    }
}
