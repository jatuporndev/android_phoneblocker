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
import android.net.Uri
import android.util.Log
import android.widget.TextView

class SMSFragment : Fragment() {
    var data = ArrayList<Data>()
    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_s_m_s, container, false)
       // data.add((Data("1", "man", "0957739456", "ดีจ้า", "25/11/2564")))
        data =getAllSms()
        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = DataAdapter(data)


        return root
    }
    class Data(
        var id: String,
        var phonenember: String,
        var message: String,
        var date: String,
        var thread_id:String
    )
    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_history_sms,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            holder.txtphone.text = data.phonenember
            holder.txtdate.text = data.date
            holder.txtmessage.text = data.message

        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
         //   var txtname: TextView = itemView.findViewById(R.id.txtnamesms)
            var txtphone: TextView = itemView.findViewById(R.id.txtphonesms)
            var txtmessage: TextView = itemView.findViewById(R.id.txtmassms)
            var txtdate: TextView = itemView.findViewById(R.id.txtdatesms)


        }
    }

    fun getAllSms(): ArrayList<Data> {
        val lstSms = ArrayList<Data>()
        val message: Uri = Uri.parse("content://sms/")
        val cr: ContentResolver = requireActivity().getContentResolver()
        val c = cr.query(message, null, null, null, null)
        while (c!!.moveToNext()){
            val body = c.getString(c.getColumnIndexOrThrow("body"))
            val id = c.getString(c.getColumnIndexOrThrow("_id"))
            val address = c.getString(c.getColumnIndexOrThrow("address"))
            val date = c.getString(c.getColumnIndexOrThrow("date"))
            val thread_id  = c.getString(c.getColumnIndexOrThrow("thread_id"))
            val callDayTime = Date(Long.valueOf(date))
            val dateFormated = SimpleDateFormat("dd/MM/yyyy HH:mm").format(callDayTime)
            Log.d("Mainactivity",address+"||"+thread_id)
            lstSms.add(Data(id,address,body,dateFormated,thread_id ))
        }


        c.close()
        return lstSms
    }
}