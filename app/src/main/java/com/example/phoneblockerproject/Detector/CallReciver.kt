package com.example.phoneblockerproject.Detector

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.phoneblockerproject.MyService
import com.example.phoneblockerproject.PhoneFragment
import com.example.phoneblockerproject.databass.DBHelper

class CallReciver : BroadcastReceiver() {
    val data = ArrayList<Data>()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceive(context: Context?, intent: Intent?) {

        allPhone(context)

        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_RINGING) {
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (data.any { it.phoneNumber == incomingNumber }) {
                Log.d("calling1", incomingNumber!!)
                val telecomManager = context!!.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ANSWER_PHONE_CALLS
                            ) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    telecomManager.endCall()
                }

            }
        }
    }

    class Data(var id: String,  var name: String,  var phoneNumber: String)
    @SuppressLint("Range")
    fun allPhone(context: Context?){
        data.clear()
        val db = DBHelper(context!!)
        val cur =db.getPhone("")
        while (cur!!.moveToNext()) {
            data.add(Data(cur.getString(0), cur.getString(1), cur.getString(2)))

        }

    }

    }




