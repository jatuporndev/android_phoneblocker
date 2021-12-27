package com.example.phoneblockerproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.phoneblockerproject.Detector.WIfiReciver
import com.example.phoneblockerproject.Fragment.HistoryFragment
import com.example.phoneblockerproject.Fragment.HomeFragment
import com.example.phoneblockerproject.Fragment.SMSFragment
import com.example.phoneblockerproject.Notification.NotificationWIfi
import com.example.phoneblockerproject.databass.DBHelper
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), WIfiReciver.NetworkStateReceiverListener  {
    var REQUEST_PERMISSION = 255
    private var networkStateReceiver: WIfiReciver? = null
    private var noti:NotificationWIfi?=null
    companion object {
         var dataphone = ArrayList<Data>()
        var datasms = ArrayList<DataSms>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        noti = NotificationWIfi()
        dataServer()
        dataSmsServer()
        permission_fn();
        setNetworkStateReceiver()
        setTransparentStatusBar()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, HomeFragment())
            transaction.commit()

            navView.setOnNavigationItemSelectedListener {
                var fm: Fragment = HomeFragment()
                when (it.itemId) {
                    R.id.nav_home -> fm = HomeFragment()
                    R.id.nav_his -> fm = HistoryFragment()
                    R.id.nav_sms ->fm= SMSFragment()

                }
                //this.supportActionBar!!.title = "Home"
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, fm)
                transaction.commit()
                return@setOnNavigationItemSelectedListener true
            }


    }
    fun Activity.setTransparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    private fun permission_fn() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SYSTEM_ALERT_WINDOW)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ANSWER_PHONE_CALLS)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALL_LOG)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)&&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
            ) {

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.READ_CALL_LOG,
                    android.Manifest.permission.ANSWER_PHONE_CALLS,
                    android.Manifest.permission.WRITE_CALL_LOG,
                    android.Manifest.permission.READ_CONTACTS,
                    android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.RECEIVE_SMS
                ), REQUEST_PERMISSION
            )
        }
        } else {

        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            permissions.forEachIndexed{
                    i, permission ->
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "โปรดอนุณาตให้เข้าถึงข้อมูล", Toast.LENGTH_SHORT).show()
                    (this.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                        .clearApplicationUserData() // note: it has a return value!

                }
            }

        }
    }

    class Data(var id:String,var name:String,var phone:String)
    @SuppressLint("Range")
    fun dataServer(){
      val db = DBHelper(this)
       val datasql = db.getAllphonespam()
        dataphone.clear()
        while(datasql.moveToNext()){
            val id = datasql.getString(datasql.getColumnIndex("id"))
            val name = datasql.getString(datasql.getColumnIndex("name"))
            val phone = datasql.getString(datasql.getColumnIndex("phoneNumber"))
            dataphone.add(Data(id,name,phone))
        }

         }
    class DataSms(var id:String,var name:String,var address:String)
    @SuppressLint("Range")
    fun dataSmsServer(){
        val db = DBHelper(this)
        val datasql = db.getAllsmspam()
        datasms.clear()
        while(datasql.moveToNext()){
            val id = datasql.getString(datasql.getColumnIndex("id"))
            val name = datasql.getString(datasql.getColumnIndex("name"))
            val phone = datasql.getString(datasql.getColumnIndex("address"))
            datasms.add(DataSms(id,name,phone))
            Log.d("main2",id+phone+name)
        }

    }
    private fun setNetworkStateReceiver(){
        networkStateReceiver = WIfiReciver(this)
        networkStateReceiver!!.addListener(this)
        applicationContext.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun onNetworkAvailable() {
      //  Toast.makeText(this, "เปิด", Toast.LENGTH_SHORT).show()
        noti?.Notify(this)

    }

    override fun onNetworkUnavailable() {
       // Toast.makeText(this, "ปิด", Toast.LENGTH_SHORT).show()
    }



}