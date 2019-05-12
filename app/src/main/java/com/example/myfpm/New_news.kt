package com.example.myfpm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_news.*

class New_news : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_news)

        upload_photo_in_new_news.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        create_news_button.setOnClickListener {
            //Выгрузка на бд новости, если успешно

            val intent = Intent(this, MyFPMpage::class.java)
            startActivity(intent)
        }
    }
}
