package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Reg2Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg2_page)
    }

    fun toMain (view: View){
        val intent = Intent(this, MyFPMpage::class.java )
        startActivity(intent)
    }
}
