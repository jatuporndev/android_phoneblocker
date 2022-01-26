package com.example.phoneblockerproject.Notification

import android.content.Context
import android.content.Intent

import com.example.phoneblockerproject.Activity.MainActivity

import android.annotation.SuppressLint
import android.app.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import com.example.phoneblockerproject.databass.DBHelper
import android.app.PendingIntent





class NotificationWIfi {
    val datawifi = ArrayList<Data>()

    fun Notify(context: Context) {

        allWifi(context)
        var wifissid = wifi(context)

        if (!datawifi.any { it.ssid ==  wifissid}){
            lateinit var notificationChannel: NotificationChannel
            lateinit var builder: Notification.Builder
            val channelId = "12345"
            val description = "Test Notification"
            val notificationIntent = Intent(context, MainActivity::class.java)
            val contentIntent = PendingIntent.getActivity(
                context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
            val intent = Intent(context, LauncherActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
                notificationChannel.lightColor = Color.BLUE
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
                builder = Notification.Builder(context, channelId).setContentTitle(wifi(context)).setContentText("เครือข่าย $wifissid ไม่ได้อยู่ในรายการที่บันทึกไว้")
                    .setSmallIcon(com.example.phoneblockerproject.R.drawable.logoghome).setLargeIcon(
                        BitmapFactory.decodeResource(context.resources, com.example.phoneblockerproject.R.drawable
                            .ic_launcher_background)).setContentIntent(pendingIntent).setContentIntent(contentIntent)
            }
            notificationManager.notify(12345, builder.build())
        }


    }

    fun wifi(context: Context):String{
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var wifiinfo: WifiInfo? = wifiManager.connectionInfo
        var ssid = wifiinfo!!.ssid.toString().drop(1).dropLast(1)

        return  "$ssid"
    }

    class Data(var id: String, var ssid: String)

    @SuppressLint("Range")
    fun allWifi(context: Context?){
        datawifi.clear()
        val db=DBHelper(context!!)
        val datwifi =db.selectwifi()
        while (datwifi.moveToNext()){
            val id = datwifi.getString(datwifi.getColumnIndex("id"))
            val ssid = datwifi.getString(datwifi.getColumnIndex("ssid"))
            datawifi.add((Data(id, ssid)))
        }


    }

}

