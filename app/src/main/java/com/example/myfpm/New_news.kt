package com.example.myfpm

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_new_news.*
import kotlinx.android.synthetic.main.activity_reg_person_page.*
import java.util.*

class New_news : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_news)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.action_bar)

        upload_photo_in_new_news.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        create_news_button.setOnClickListener {
            createNews()
        }
    }

    private fun createNews(){

        FirebaseAuth.getInstance().addAuthStateListener {

            val newsUUID = UUID.randomUUID().toString()
            val userUid = it.currentUser!!.uid
            val text = input_news.text.toString()
            val imageFileName = UUID.randomUUID()
            var imageUrl: String

            val localDate = Calendar.getInstance().time

            val date = Date(localDate.year, localDate.month, localDate.date,
                localDate.hours, localDate.minutes)

            if(selectedPhotoUri != null){

                val bmp = ImageDesigner()
                    .handleSamplingAndRotationBitmap(this, selectedPhotoUri!!)

                if(bmp == null){
                    imageUrl = "null"
                    pushToFirebase(newsUUID, userUid, date, imageUrl, text)
                }

                val file = ImageDesigner().saveBitmap(bmp!!)

                val ref = FirebaseStorage.getInstance()
                    .getReference("/images/$imageFileName")
                ref.putFile(Uri.fromFile(file)).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {

                        imageUrl = it.toString()

                        pushToFirebase(newsUUID, userUid, date, imageUrl, text)
                    }
                }
            }
            else {
                imageUrl = "null"
                pushToFirebase(newsUUID, userUid, date, imageUrl, text)
            }
        }
    }

    private fun pushToFirebase(newsUUID: String, userUid: String, date:Date,
                               imageUrl: String, text: String){
        val new = News(newsUUID, userUid, date, imageUrl, text)

        FirebaseFirestore.getInstance().collection("news").add(new)
            .addOnCompleteListener {
                if(!it.isSuccessful){
                    Toast.makeText(this, "Error, news not created",
                        Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                Toast.makeText(this, "News are created",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MyFPMpage::class.java)
                startActivity(intent)
            }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data!!

            val bitmap = ImageDesigner().handleSamplingAndRotationBitmap(this,
                selectedPhotoUri!!)


           news_photo_selected.setImageBitmap(bitmap)
            upload_photo_in_new_news.alpha =0f
        }

    }




}
