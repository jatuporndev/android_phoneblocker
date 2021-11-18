package com.example.phoneblockerproject.Detector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class CallReciver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.getStringExtra(TelephonyManager.EXTRA_STATE)!! == TelephonyManager.EXTRA_STATE_RINGING){
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Log.d("calling",incomingNumber.toString())
        }
    }
}