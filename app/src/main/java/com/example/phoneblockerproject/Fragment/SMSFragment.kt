package com.example.phoneblockerproject.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
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

import android.content.ContentResolver
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.databass.DBHelper
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SMSFragment : Fragment() {
    var data = ArrayList<Data>()
    var datablock = ArrayList<DataBlock>()
    var serverData = ArrayList<MainActivity.DataSms>()
    var recyclerView: RecyclerView? = null
    var search:SearchView?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_s_m_s, container, false)

        search = root.findViewById(R.id.search)
        search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {//กดปุ่มไปเพื่อค้นหา
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {//คนหาทุกครั้งที่พิมพ์
                var title = newText//คำที่ค้นหา
                if (!title.isNullOrEmpty()){
                    recyclerView!!.adapter = DataAdapter(data.filter { it.phonenember.contains(title) || it.message.contains(title)} )
                }else{
                    recyclerView!!.adapter = DataAdapter(data)
                }
                return false
            }
        })
        data =getAllSms()
        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = DataAdapter(data)
        serverData = MainActivity.datasms
        getdata()
        return root
    }

    @SuppressLint("Range")
    fun getdata(){
        datablock.clear()
        val db=DBHelper(requireContext())
        val datasms =db.selectsms()
        while (datasms.moveToNext()){
            val id = datasms.getString(datasms.getColumnIndex("id"))
            val address = datasms.getString(datasms.getColumnIndex("address"))
            val name = datasms.getString(datasms.getColumnIndex("name"))
            datablock.add(DataBlock(id,address,name))
        }
    }

    class Data(
        var id: String,
        var phonenember: String,
        var message: String,
        var date: String,
        var thread_id:String
    )
    class DataBlock(
        var id: String,
        var address: String,
        var name: String

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

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            holder.txtphone.text = data.phonenember
            holder.txtdate.text = data.date
            holder.txtmessage.text = data.message
            if (datablock.any { it.address == data.phonenember }){
                holder.txtphone.text = datablock.find { it.address ==data.phonenember}!!.name+" (${data.phonenember})"
            }else{
                if (serverData.any {it.address ==data.phonenember  }){
                    holder.txtphone.text = serverData.find { it.address ==data.phonenember}!!.name+" (${data.phonenember})"
                }
            }


            holder.consms.setOnLongClickListener() {

                Dialogmenu(data.thread_id,data.phonenember)
                return@setOnLongClickListener true
            }
        }
//
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
            var consms: ConstraintLayout = itemView.findViewById(R.id.consms)
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

    private lateinit var alertDialomenug: AlertDialog
    fun Dialogmenu(thread_id:String,address:String) {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_menu_history, null)
        var conDelete:ConstraintLayout=dialogView.findViewById(R.id.constraintdelete)
        var conReport:ConstraintLayout=dialogView.findViewById(R.id.conreport)
        var conGolist:ConstraintLayout=dialogView.findViewById(R.id.constraintgolist)
        var name = ""
        conDelete.visibility = View.GONE
        conGolist.setOnClickListener {

            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("ระบุชื่อ")
            val input = EditText(requireContext())
            input.setHint("ระบุชื่อ")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("ตกลง", DialogInterface.OnClickListener { dialog, which ->
                name=input.text.toString()
                if(name.isEmpty()){
                    name="ไม่มีชื่อ"
                }else{
                    name=input.text.toString()
                }
                val db = DBHelper(requireContext())
                db.addsms(thread_id,address,name)
                val fragmentTransaction = requireActivity().
                supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, BlockerMessageFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                alertDialomenug.dismiss()

            })
            builder.setNegativeButton("ยกเลิก") { dialog, which -> dialog.cancel() }
            builder.show()

        }
        conReport.setOnClickListener{

            Dialogreport(address)

        }


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialomenug = dialogBuilder.create();
        alertDialomenug.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialomenug.show()
    }

    private lateinit var alertDialomenug2: AlertDialog
    fun Dialogreport(address: String) {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_report_sms, null)
        var txtname:TextView=dialogView.findViewById(R.id.txtname)
        var spinner:Spinner=dialogView.findViewById(R.id.spinner)
        var btncon:Button=dialogView.findViewById(R.id.btncon)
        var btnback:Button=dialogView.findViewById(R.id.btnback)
        txtname.setText(address)

        val adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.city_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter


        btncon.setOnClickListener {
            val text: String = spinner.getSelectedItem().toString()
                addreport(address,text)
            alertDialomenug2.dismiss()

        }
        btnback.setOnClickListener {
            alertDialomenug2.dismiss()

        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialomenug2 = dialogBuilder.create();
        alertDialomenug2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialomenug2.show()
    }

    private fun addreport(address: String,text:String)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.AddReport_url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("address",address)
            .add("detail",text)
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Toast.makeText(context, "รายงานสำเร็จ", Toast.LENGTH_LONG).show()
                        response.code

                    }
                    else{

                    }

                } catch (e: JSONException) {

                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
                response.code
            }
        } catch (e: IOException) {
            Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

}