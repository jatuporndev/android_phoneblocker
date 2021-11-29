package com.example.phoneblockerproject.Fragment

import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.R
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.provider.Telephony.Sms

import android.content.ContentResolver





class SMSFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_s_m_s, container, false)



        return root
    }

}