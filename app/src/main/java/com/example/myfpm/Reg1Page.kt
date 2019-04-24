package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.sql.Connection

class Reg1Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg1_page)
        val conn = ConnectionClass().Connect()
    }

    fun obClick2 (view: View){
        val intent = Intent(this, MyFPMpage::class.java )
        startActivity(intent)
    }
}
