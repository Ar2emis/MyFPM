package com.example.myfpm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import java.sql.Date

class Reg2Page : AppCompatActivity(), AdapterView.OnItemClickListener {

    var years: List<() -> String> = listOf(){"year"}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg2_page)
    }

    fun toMain (view: View){
        val intent = Intent(this, MyFPMpage::class.java )
        startActivity(intent)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
