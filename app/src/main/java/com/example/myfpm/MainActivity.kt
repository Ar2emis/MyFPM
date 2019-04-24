package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick (view: View){
        val intent = Intent(this, Reg1Page::class.java )
        startActivity(intent)
    }

    fun onClick2 (view: View){
        val intent = Intent(this, MainProjectList::class.java )
        startActivity(intent)
    }
}
