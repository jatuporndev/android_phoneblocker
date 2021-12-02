package com.example.phoneblockerproject.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.R
import com.example.phoneblockerproject.databass.DBHelper

class BlockerMessageFragment : Fragment() {
    var data = ArrayList<Data>()
    var recyclerView: RecyclerView? = null
    var imgback: ImageView? = null
    var imgsms: ImageView?=null
    //box
    var editTextPhone :EditText?=null
    var editTextName:EditText?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_blocker_message, container, false)
        recyclerView = root.findViewById(R.id.recyclerView)

        imgback = root.findViewById(R.id.imgback)
        imgback?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        imgsms = root.findViewById(R.id.imgsms)
        imgsms?.setOnClickListener{
            showCustomDialog()
        }
        getdata()



        return root
    }


    /*@SuppressLint("Range")
    fun getdata(){
        data.clear()
        val db=DBHelper(requireContext())
        val datatest =db.datafromTest()
        while (datatest.moveToNext()){
            val name = datatest.getString(datatest.getColumnIndex("name"))
            val id = datatest.getString(datatest.getColumnIndex("id"))
            data.add((Data(id , name, "0957739456", "ดีจ้า", "25/11/2564")))

        }
        recyclerView!!.adapter = DataAdapter(data)
    }*/

    @SuppressLint("Range")
   fun getdata(){
       data.clear()
       val db=DBHelper(requireContext())
       val datasms =db.selectsms()
       while (datasms.moveToNext()){
           val id = datasms.getString(datasms.getColumnIndex("id"))
           val thread_id = datasms.getString(datasms.getColumnIndex("thread_id"))
           val address = datasms.getString(datasms.getColumnIndex("address"))
           val name = datasms.getString(datasms.getColumnIndex("name"))
           data.add((Data(id,name,address,thread_id)))

       }
       recyclerView!!.adapter = DataAdapter(data)
   }



    class Data(
        var id: String,
        var name: String,
        var phonenember: String,
        var message: String,
        //var date: String
    )
    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_blockermessage,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("txt", "x3")
            val data = list[position]
            holder.data = data
            holder.txtname.text = data.name
            holder.txtphone.text = data.phonenember
           // holder.txtdate.text = data.date
            holder.delete.setOnClickListener {
                val db = DBHelper(requireContext())
                db.deletesmsblock(data.id)
                getdata()

            }

        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var txtname: TextView = itemView.findViewById(R.id.txtname)
            var txtphone: TextView = itemView.findViewById(R.id.txtphone)
            //var txtdate: TextView = itemView.findViewById(R.id.txtdate)
            var delete:ImageButton = itemView.findViewById(R.id.imgsms)

        }
    }

    private lateinit var alertDialog: AlertDialog
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_add_sms, null)
        editTextPhone = dialogView.findViewById(R.id.editTextPhone)
        editTextName = dialogView.findViewById(R.id.editTextName)
        var btnConfirm : Button =dialogView.findViewById(R.id.btnConfirm)
        var imghis : ImageButton =dialogView.findViewById(R.id.imghis)

        imghis.setOnClickListener {

            showCustomDialogCon()

        }
        //
        btnConfirm.setOnClickListener {
            //con
            //ประกาศทุกครั้งที่จะใช้
            val db = DBHelper(requireContext())
          //  db.adddataTable_Test(editTextName!!.text.toString(),"test@test")

        }


       // btncancel.setOnClickListener {
         //   alertDialog.dismiss()
       // }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    var dialogCon: Dialog? =null
    @SuppressLint("ResourceAsColor")
    fun showCustomDialogCon() {
        var view: View = layoutInflater.inflate(R.layout.fragment_history_message, null)
        var back:ImageView = view.findViewById(R.id.imgbackc)
        dialogCon = Dialog(requireContext(), android.R.style.ThemeOverlay_DeviceDefault_Accent_DayNight)
        dialogCon!!.setContentView(view)
        back?.setOnClickListener {
            dialogCon!!.dismiss()
        }
        dialogCon!!.show()

    }
}