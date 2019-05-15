package com.example.myfpm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_reg_person_page.*
import android.graphics.Bitmap
import androidx.appcompat.app.ActionBar
import com.squareup.picasso.Picasso
import io.grpc.internal.Stream
import java.io.OutputStream as OutputStream1


class RegPersonPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_person_page)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.action_bar)

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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            intent.putExtra("name", name)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("creatorUid", name)
            intent.putExtra("surname", surname)
            intent.putExtra("phone", phone)
            intent.putExtra("imageUri", selectedPhotoUri)

            startActivity(intent)
        }

        upload_new_photo_button.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data!!

            var bitmap = ImageDesigner().handleSamplingAndRotationBitmap(this, selectedPhotoUri!!)

            val height = bitmap!!.height
            val width = bitmap.width

            var dimension = height
            if(height > width)
                dimension = width

            bitmap = Bitmap.createBitmap(bitmap,
                (width - dimension) / 2,
                (height - dimension) / 2,
                dimension, dimension)

            upload_new_photo_button.background = BitmapDrawable(bitmap)
        }

    }

    private fun checkData(name: String, surname: String, phone: String): Boolean{
        var isDataCorrect = true

        if (name.isEmpty()) {
            name_reg_edit_text.error = "Please enter creatorUid"
            name_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Empty creatorUid!!!")
        }

        if (surname.isEmpty()) {
            surname_reg_edit_text.error = "Please enter surname"
            surname_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Empty surname!!!")
        }

        if (phone.isNotEmpty() && !Patterns.PHONE.matcher(phone).matches()) {
            phone_reg_edit_text.error = "Please enter valid phone number"
            phone_reg_edit_text.requestFocus()
            isDataCorrect = false
            Log.d("RegPersonPage", "!!!Invalid phone number!!!")
        }

        return isDataCorrect
    }
}
