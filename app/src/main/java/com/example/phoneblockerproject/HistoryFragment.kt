package com.example.phoneblockerproject

import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.databass.DBHelper
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {
    var data = ArrayList<Data>()
    var recyclerView: RecyclerView? = null

    //popupmenu
    var conDelete:ConstraintLayout?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = root.findViewById(R.id.recyclerView)

        getdata()
        return root
    }

    fun getdata(){
        data.clear()
        data = getCalllog()
        recyclerView!!.adapter = DataAdapter(data)
    }


    class Data(var id:String,var number: String, var type: String, var date: String, var duration: String, var name: String)

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_history,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("txt", "x3")
            val data = list[position]
            holder.data = data
            holder.txtname2.text = data.name
            holder.txtphone2.text = data.number
            holder.txtdate.text = data.date
            holder.conmenu.setOnLongClickListener() {
                showCustomDialog()
                conDelete?.setOnClickListener {
                    DeleteCallById(data.id,data.number)
                    alertDialog.dismiss()
                }
                return@setOnLongClickListener true
            }
            if(data.type=="MISSED"){
                holder.txttype.text="สายที่ไม่ได้รับ"
            }else{
                holder.txttype.text="ระยะเวลาโทร "+data.duration
            }
            holder.imgicon.visibility = View.GONE
           // holder.imgicon2.visibility = View.GONE


        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var txtname2: TextView = itemView.findViewById(R.id.txtname2)
            var txtphone2: TextView = itemView.findViewById(R.id.txtphone2)
            var txtdate: TextView = itemView.findViewById(R.id.txtdate)
            var conmenu: ConstraintLayout = itemView.findViewById(R.id.conmenu)
            var imgicon: ImageView = itemView.findViewById(R.id.imgicon)
            var imgicon2: ImageView = itemView.findViewById(R.id.imgicon2)
            var txttype :TextView=itemView.findViewById(R.id.txtyype)
        }
    }

    private lateinit var alertDialog: AlertDialog
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_menu_history, null)
         conDelete=dialogView.findViewById(R.id.constraintdelete)
        var conReport:ConstraintLayout=dialogView.findViewById(R.id.constraintreport)
        var conGolist:ConstraintLayout=dialogView.findViewById(R.id.constraintgolist)


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)


        alertDialog = dialogBuilder.create();
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun getCalllog():ArrayList<Data> {
        var call= ArrayList<Data>()
        val cursor: Cursor? = requireContext().contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC"
        )
        val number: Int = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
        val type: Int = cursor.getColumnIndex(CallLog.Calls.TYPE)
        val date: Int = cursor.getColumnIndex(CallLog.Calls.DATE)
        val duration: Int = cursor.getColumnIndex(CallLog.Calls.DURATION)
        val Name: Int = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
        val id: Int = cursor.getColumnIndex(CallLog.Calls._ID)
        var i=0

        while (cursor.moveToNext()) {
            val phNumber: String = cursor.getString(number)
            val callType: String = cursor.getString(type)
            val callDate: String = cursor.getString(date)
            val callid: String = cursor.getString(id)
            val callDayTime = Date(Long.valueOf(callDate))
            val dateFormated = SimpleDateFormat("dd/MM/yyyy HH:mm").format(callDayTime)
            var name :String="ไม่มีชื่อ"
            if (cursor.getString(Name)!=null){
                name  = cursor.getString(Name)
            }
            val callDuration: String = cursor.getString(duration)
            var second = callDuration.toLong()
            var  minutes = (second % 3600) / 60;
            var seconds = second % 60;
            var fomat:String=""
            if(second<60){
                fomat = seconds.toString()+ "วินาที "
            }else if(second>60){
                fomat = minutes.toString()+ "นาที "+seconds.toString()+" วินาที"
            }
            var dir: String? = null
            val dircode = callType.toInt()
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }
            if (i==100){break}

            call.add(Data(callid,phNumber, dir.toString(), dateFormated.toString(), fomat, name))
            i++

        }


        cursor.close()
        return call
    }

    fun DeleteCallById(idd: String,phone:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("ต้องการจะบกเลิกบล็อกเบอร์ $phone หรือไม่?")
                .setCancelable(false)
                .setPositiveButton("ใช่") { dialog, id ->
                    // Delete selected note from database
                    requireContext().contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID + " = ? ", arrayOf(idd))
                    Toast.makeText(context, "สำเร็จ", Toast.LENGTH_LONG).show()
                    getdata()
                }
                .setNegativeButton("ยกเลิก") { dialog, id ->
                    dialog.dismiss()
                }
        val alert = builder.create()
        alert.show()

    }


}