package com.example.phoneblockerproject.Detector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast


class CallReciver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_RINGING) {
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (incomingNumber == "0827893829") {
                Log.d("calling1", incomingNumber)
                audioManager.setStreamMute(AudioManager.STREAM_RING, true)

            }
        }
        if(intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_OFFHOOK ||
                intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_IDLE){

            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        }

    }



}
