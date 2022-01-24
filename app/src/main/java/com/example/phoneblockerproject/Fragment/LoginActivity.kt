package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    val appPreference:String = "appPrefer"
    val memberIdPreference:String = "memberIdPref"
    val usernamePreference:String = "usernamePref"
    val userstatus:String ="0"
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
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val editUsername = findViewById<EditText>(R.id.editTextName)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val url = getString(R.string.root_url) + getString(R.string.login_url)
            val okHttpClient = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()
                .add("username", editUsername.text.toString())
                .add("password", editPassword.text.toString())
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val obj = JSONObject(response.body!!.string())
                        val userID = obj["id"].toString()
                        val username = obj["username"].toString()

                        //Create shared preference to store user data
                        val sharedPrefer: SharedPreferences =
                            getSharedPreferences(appPreference, Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPrefer.edit()

                        editor.putString(memberIdPreference, userID)
                        editor.putString(usernamePreference, username)
                        editor.putString(userstatus, "0")
                        editor.commit()

                        //return to login page
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    response.code
                    Toast.makeText(applicationContext, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }


}