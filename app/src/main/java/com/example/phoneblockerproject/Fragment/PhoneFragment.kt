package com.example.phoneblockerproject.Fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.R
import com.example.phoneblockerproject.databass.DBHelper
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PhoneFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var imgaddphone:ImageView?=null
    var imgback:ImageView?=null
    val data = ArrayList<Data>()
    var searchView:SearchView?=null
    var txtempty:TextView?=null
    var phonenumber:String?=null
    var phonename:String?=null

    //box
    var editTextPhone :EditText?=null
    var editTextName:EditText?=null

    var recyclerViewConract:RecyclerView?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =inflater.inflate(R.layout.fragment_phone, container, false)
        imgaddphone = root.findViewById(R.id.imgaddphone)
        recyclerView = root.findViewById(R.id.recyclerView)
        imgback = root.findViewById(R.id.imgback)
        searchView =root.findViewById(R.id.SearchView)
        txtempty = root.findViewById(R.id.txtempty)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var title = newText
                allPhone(title)
                return false
            }
        })


        imgaddphone?.setOnClickListener(){
            showCustomDialog()
        }
        allPhone("")

        imgback?.setOnClickListener(){
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return root
    }

    private lateinit var alertDialog: AlertDialog
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_add_phone, null)
         editTextPhone = dialogView.findViewById(R.id.editTextPhone)
         editTextName = dialogView.findViewById(R.id.editTextName)
        val btnConfirm: Button = dialogView.findViewById(R.id.btnConfirm)
        var btncancel :Button=dialogView.findViewById(R.id.btnConfirm2)
        var imgcontext:ImageButton=dialogView.findViewById(R.id.imgcontext)
        var imgphonehis:ImageButton=dialogView.findViewById(R.id.imgcontext2)

        imgcontext.setOnClickListener {
            requestPermission()
        }
        imgphonehis.setOnClickListener {

            showCustomDialogCon()

        }

        btnConfirm.setOnClickListener {
            if(!editTextPhone?.text.toString().isNullOrEmpty()){
                if (editTextName?.text.toString().isNullOrEmpty()){editTextName?.setText("ไม่มีชื่อ")}
                val db = DBHelper(requireContext())
                db.addPhone(editTextName?.text.toString(), editTextPhone?.text.toString())
                allPhone("")
                alertDialog.dismiss()
            }else{
                Toast.makeText(context, "ระบุเบอร์!!", Toast.LENGTH_LONG).show()
            }
        }
        btncancel.setOnClickListener {
            alertDialog.dismiss()
        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }


    class Data(
            var id: String, var name: String, var phoneNumber: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_phone,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            holder.txtname.text = data.name
            holder.txtphone.text = data.phoneNumber
            holder.delete.setOnClickListener {
                deletePhone(data.phoneNumber, data.id)
            }
            holder.con.setOnLongClickListener() {
                Dialogmenu(data.phoneNumber,data.id)
                return@setOnLongClickListener true
            }

        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var con:ConstraintLayout = itemView.findViewById(R.id.conme)
            var txtname: TextView = itemView.findViewById(R.id.txtname)
            var txtphone: TextView = itemView.findViewById(R.id.txtphone)
            var delete:ImageButton = itemView.findViewById(R.id.deleteimgbuttom)

        }
    }

    @SuppressLint("Range")
    fun allPhone(text: String){
        data.clear()
        val db = DBHelper(requireContext())
        val cur =db.getPhone(text)
        while (cur!!.moveToNext()) {
            data.add(Data(cur.getString(0), cur.getString(1), cur.getString(2)))
        }
        if(!data.isNullOrEmpty()){
            txtempty?.visibility = View.GONE
        }else{
            txtempty?.visibility = View.VISIBLE
        }
        recyclerView!!.adapter = DataAdapter(data)
    }

    fun deletePhone(phone: String, idp: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("ต้องการจะบกเลิกบล็อกเบอร์ $phone หรือไม่?")
            .setCancelable(false)
            .setPositiveButton("ใช่") { dialog, id ->
                // Delete selected note from database
                val db = DBHelper(requireContext())
                db.deletePhone(idp)
                allPhone("")
                Toast.makeText(context, "สำเร็จ", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("ยกเลิก") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    fun requestPermission(){
        if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    1
            )
        }else{
            val intent = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            startActivityForResult(intent, 125)

        }
    }
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (android.R.attr.data != null) {
                val result: Uri = data!!.data!!
                val c = requireContext().contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone._ID + "=?",
                        arrayOf(result.lastPathSegment),
                        null
                )
                if (c!!.count >= 1 && c!!.moveToFirst()) {
                    phonenumber = c!!.getString(c!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    phonename = c!!.getString(c!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    if ((phonenumber != "" || phonename != "")) {

                        editTextName?.setText(phonename)
                        editTextPhone?.setText(phonenumber)
                    }
                }
            }
        }else{

        }

    }



    private fun getCalllog():ArrayList<Call> {
        var call= ArrayList<Call>()
        val cursor: Cursor? = requireContext().contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC"
        )
        val number: Int = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
        val type: Int = cursor.getColumnIndex(CallLog.Calls.TYPE)
        val date: Int = cursor.getColumnIndex(CallLog.Calls.DATE)
        val duration: Int = cursor.getColumnIndex(CallLog.Calls.DURATION)
        val Name: Int = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
        var i=0

            while (cursor.moveToNext()) {
                val phNumber: String = cursor.getString(number)
                val callType: String = cursor.getString(type)
                val callDate: String = cursor.getString(date)
                val callDayTime = Date(Long.valueOf(callDate))
                val dateFormated = SimpleDateFormat("dd/MM/yyyy").format(callDayTime)
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

                 call.add(Call(phNumber, dir.toString(), dateFormated.toString(), fomat, name))
                i++

            }


        cursor.close()
        return call
    }


    var dialogCon: Dialog? =null
    @SuppressLint("ResourceAsColor")
    fun showCustomDialogCon() {
        var view: View = layoutInflater.inflate(R.layout.fragment_read_contact, null)
        var back:ImageView = view.findViewById(R.id.imgbackc)
        recyclerViewConract=view.findViewById(R.id.recyview)
        dialogCon = Dialog(requireContext(), android.R.style.ThemeOverlay_DeviceDefault_Accent_DayNight)
        dialogCon!!.setContentView(view)
        back?.setOnClickListener {
            dialogCon!!.dismiss()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogCon!!.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialogCon!!.window?.statusBarColor = R.color.purple_500;
        }
        dialogCon!!.show()
        var data = getCalllog()
        recyclerViewConract!!.adapter = DataAdapter2(data)
    }



    class Call(var number: String, var type: String, var date: String, var duration: String, var name: String)
    internal inner class DataAdapter2(private val list: ArrayList<Call>) :
            RecyclerView.Adapter<DataAdapter2.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_contact,
                    parent, false
            )
            return ViewHolder(view)
        }

        @RequiresApi(31)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            holder.txtname.text = data.name
            holder.txtphone.text=data.number
            holder.date.text=data.date
            if(data.type=="MISSED"){
                holder.txttype.text="สายที่ไม่ได้รับ"
            }else{
              holder.txttype.text="ระยะเวลาโทร "+data.duration
            }
            holder.con.setOnClickListener {
                editTextName?.setText(data.name)
                editTextPhone?.setText(data.number)
                dialogCon!!.dismiss()
            }

        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            var data: Call? = null
            var con :ConstraintLayout = itemView.findViewById(R.id.conmenu)
            var txtname: TextView = itemView.findViewById(R.id.txtname2)
            var txtphone: TextView = itemView.findViewById(R.id.txtphone2)
            var date:TextView = itemView.findViewById(R.id.txtdate)
            var txttype:TextView = itemView.findViewById(R.id.txtype)

        }
    }
    private lateinit var alertDialomenug: AlertDialog
    fun Dialogmenu(phoneNumber:String,id:String) {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_menu_history, null)
        var conDelete:ConstraintLayout=dialogView.findViewById(R.id.constraintdelete)
        var conReport:ConstraintLayout=dialogView.findViewById(R.id.constraintreport)
        var conGolist:ConstraintLayout=dialogView.findViewById(R.id.constraintgolist)

        conGolist.visibility=View.GONE
        conDelete.setOnClickListener {
            deletePhone(phoneNumber, id)
            alertDialomenug.dismiss()
        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialomenug = dialogBuilder.create();
        alertDialomenug.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialomenug.show()
    }


}