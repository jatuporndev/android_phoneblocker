package com.example.phoneblockerproject.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.phoneblockerproject.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class RegisterActivity : AppCompatActivity() {
    var editTextName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var editTextConPassword: EditText? = null
    var btnCreate: Button? = null
    var checkBox:CheckBox?=null

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val txtbacklogin = findViewById<TextView>(R.id.txtbacklogin)
        txtbacklogin.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        val imgback = findViewById<ImageView>(R.id.imgback2)
        imgback.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConPassword = findViewById(R.id.editTextPassword2)
        btnCreate = findViewById(R.id.btnok)
        checkBox = findViewById(R.id.checkBox)

        checkBox?.setOnCheckedChangeListener { compoundButton, b ->
            btnCreate?.isEnabled = b

        }
        btnCreate?.isEnabled=false

        btnCreate!!.setOnClickListener {

            if (editTextPassword?.text.toString() == editTextConPassword?.text.toString()){
                Register()
            }else{
                Toast.makeText(this, "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show()
            }


        }
        ReadRule()
    }

    private fun Register()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.member_url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("username", editTextName?.text.toString())
            .add("email", editTextEmail?.text.toString())
            .add("password", editTextPassword?.text.toString())
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Toast.makeText(this, "สมัครสมาชิกเรียบร้อยแล้ว", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                response.code
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun ReadRule(){
        val txtread = findViewById<TextView>(R.id.txtrule)
        var view: View = layoutInflater.inflate(R.layout.read,null)
        var btnreaded:Button = view.findViewById(R.id.btnreaded)
        var txtpp:TextView=view.findViewById(R.id.txtpp)

        var text:String=""
        try {
            var io: InputStream = assets.open("pp.txt")
            var size:Int = io.available()
            var buffer=ByteArray(size)
            io.read(buffer)
            io.close()
            text= String(buffer)
        }catch (ex : IOException){
            ex.printStackTrace()
        }
        txtpp.text=text

        var dialog: Dialog = Dialog(this,android.R.style.ThemeOverlay_DeviceDefault_Accent_DayNight)
        dialog.setContentView(view)
        txtread?.setOnClickListener {
            dialog.show()
        }
        btnreaded?.setOnClickListener {
            dialog.dismiss()
            btnCreate?.isEnabled = true
            checkBox?.isChecked = true
        }
    }
}