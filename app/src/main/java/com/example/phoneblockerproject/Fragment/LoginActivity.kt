package com.example.phoneblockerproject.Fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.phoneblockerproject.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val txtregister = findViewById<TextView>(R.id.txtregister)
        txtregister.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val imgback = findViewById<ImageView>(R.id.imgback)
        imgback.setOnClickListener{
            startActivity(Intent(this, UsersFragment::class.java))
        }

    }

}