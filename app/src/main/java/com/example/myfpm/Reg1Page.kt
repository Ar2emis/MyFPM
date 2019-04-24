package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Reg1Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg1_page)
    }

    fun ObClick2 (view: View){
        val intent = Intent(this, Reg2Page::class.java )
        startActivity(intent)
    }
}
