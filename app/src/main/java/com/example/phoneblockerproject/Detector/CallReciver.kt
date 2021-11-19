package com.example.phoneblockerproject.Detector

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phoneblockerproject.MyService


class CallReciver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceive(context: Context?, intent: Intent?) {

        val audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_RINGING) {
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (incomingNumber == "0957739456") {
                Log.d("calling1", incomingNumber)
                audioManager.setStreamMute(AudioManager.STREAM_RING, true)
                val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
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
        if(intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_OFFHOOK ||
                intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_IDLE){

            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        }


    }


    }




