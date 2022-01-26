package com.example.phoneblockerproject.Detector

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.provider.Telephony.Sms
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.phoneblockerproject.Activity.MainActivity
import com.example.phoneblockerproject.R
import com.example.phoneblockerproject.databass.DBHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CallReciver : BroadcastReceiver() {
    val data = ArrayList<Data>()
    val datasms = ArrayList<Data>()

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0


    override fun onReceive(context: Context?, intent: Intent?) {

        allPhone(context)
        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE) != null){
            callblocker(context, intent)
        }else{
            smsCheck(context,intent)
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
                    val name = data.find { it.phoneNumber == incomingNumber}?.name!!
                    noti(context,intent,"บล็อกเบอร์","คุณได้ทำการบล็อกเบอร์ $incomingNumber($name) แล้ว")
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
        datasms.clear()
        val db = DBHelper(context!!)

        val cur =db.getPhone("")
        while (cur!!.moveToNext()) {
            data.add(Data(cur.getString(0), cur.getString(1), cur.getString(2)))
        }
        val cursms  = db.selectsms()
        while (cursms.moveToNext()){
            datasms.add(Data(cursms.getString(0), cursms.getString(3), cursms.getString(2)))
            Log.d("txt",cursms.getString(0)+ cursms.getString(3)+ cursms.getString(2))
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
    fun noti(context: Context?, intent: Intent?,Title: String,Text: String){

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setContentTitle(Title)
            .setContentText(Text)
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

    fun smsCheck(context: Context?, intent: Intent?){

        val datasmsSever = MainActivity.datasms
       for(sms in Sms.Intents.getMessagesFromIntent(intent)) {
          val incomingsms_address=  sms.displayOriginatingAddress
           val incomingsms_body=  sms.messageBody
           if(datasms.any{it.phoneNumber == incomingsms_address}){
               val name = datasms.find{it.phoneNumber == incomingsms_address}!!.name
               val address = datasms.find{it.phoneNumber == incomingsms_address}!!.phoneNumber
               noti(context,intent,"SMS จาก $name($address)",incomingsms_body)
           }else{
               if(datasmsSever.any{it.address == incomingsms_address}){
                   val name = datasmsSever.find{it.address == incomingsms_address}!!.name
                   val address = datasmsSever.find{it.address == incomingsms_address}!!.address
                   noti(context,intent,"SMS จาก $name($address)",incomingsms_body)
               }
           }

       }

    }

    }









