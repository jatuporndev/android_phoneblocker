package com.example.phoneblockerproject.Detector

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.app.TaskStackBuilder.create
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface.create
import android.media.MediaPlayer.create
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.MbmsDownloadSession.create
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.R
import com.example.phoneblockerproject.databass.DBHelper
import com.google.android.material.internal.ContextUtils.getActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.tapadoo.alerter.Alerter
import android.os.Bundle
import android.telephony.SmsManager
import okhttp3.internal.notify
import android.app.Activity
import android.telephony.SmsMessage
import androidx.core.content.res.ResourcesCompat.FontCallback.getHandler

import android.os.UserHandle

import android.Manifest.permission
import androidx.core.content.res.ResourcesCompat.FontCallback
import android.content.Context.ALARM_SERVICE

import androidx.core.content.ContextCompat.getSystemService

import android.app.AlarmManager

import android.app.PendingIntent
import android.app.NotificationManager
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import java.lang.Exception
import android.provider.Telephony.Sms



@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CallReciver : BroadcastReceiver() {
    val data = ArrayList<Data>()

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0


    override fun onReceive(context: Context?, intent: Intent?) {

        allPhone(context)
        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE) != null){
            callblocker(context, intent)
        }else{


        }


    }


    fun callblocker(context: Context?, intent: Intent?){
        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_RINGING) {
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (data.any { it.phoneNumber == incomingNumber }) {

                val telecomManager = context!!.getSystemService(Context.TELECOM_SERVICE) as TelecomManager


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ANSWER_PHONE_CALLS
                            ) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }

                    telecomManager.endCall()//ตัดสาย
                    addhistoryphone(data.find { it.phoneNumber == incomingNumber}?.name!!,incomingNumber!!,context)


                    noti(context,intent,incomingNumber,data.find { it.phoneNumber == incomingNumber}?.name!!)
                    Log.d("calling1", (data.find { it.phoneNumber == incomingNumber}?.name!!))//ชื่อ
                    createNotificationChannel(context)

                }

            }
        }
    }

    class Data(var id: String, var name: String, var phoneNumber: String)
    @SuppressLint("Range")
    fun allPhone(context: Context?){
        data.clear()
        val db = DBHelper(context!!)
        val cur =db.getPhone("")
        while (cur!!.moveToNext()) {
            data.add(Data(cur.getString(0), cur.getString(1), cur.getString(2)))

        }
    }

    @SuppressLint("SimpleDateFormat")
    fun addhistoryphone(name: String, phoneNumber: String, context: Context?){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val currentDate = sdf.format(Date())
        Log.d("Testt",currentDate)
        val db= DBHelper(context!!)
        db.addhistory(name,phoneNumber,currentDate.toString(),"0")

    }


    @SuppressLint("RestrictedApi")
    fun noti(context: Context?, intent: Intent?,phoneNumber: String,name: String){

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setContentTitle("บล็อกเบอร์")
            .setContentText("คุณได้ทำการบล็อกเบอร์ $phoneNumber($name) แล้ว")
            .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()


        val notificationManager = NotificationManagerCompat.from(context)
        //Code แจ้งเตือนด้านหน้า
        notificationManager.notify(NOTIFICATION_ID, notification)


    }
    fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    }









