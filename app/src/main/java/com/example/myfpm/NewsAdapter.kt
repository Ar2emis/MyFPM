package com.example.myfpm

import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.method.TextKeyListener.clear



class NewsAdapter: RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return 10
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
    val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.news, p0, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}

class News(val name: String, val text: String, val image: Image){

}