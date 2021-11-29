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
import android.widget.Button


class SMSFragment : Fragment() {
    var btnhismessage:Button?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_s_m_s, container, false)

        btnhismessage = root.findViewById(R.id.btnhismessage)
        btnhismessage?.setOnClickListener(){
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HistoryMessageFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return root
    }

}