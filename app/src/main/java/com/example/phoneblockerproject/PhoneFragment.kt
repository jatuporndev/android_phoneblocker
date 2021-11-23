package com.example.phoneblockerproject

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.databass.DBHelper


class PhoneFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var imgaddphone:ImageView?=null
    var imgback:ImageView?=null
    val data = ArrayList<Data>()
    var searchView:SearchView?=null
    var txtempty:TextView?=null
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
              var  title=newText
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
        val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val btnConfirm: Button = dialogView.findViewById(R.id.btnConfirm)
        var btncancel :Button=dialogView.findViewById(R.id.btnConfirm2)

        btnConfirm.setOnClickListener {
            val db = DBHelper(requireContext())
            db.addPhone(editTextName.text.toString(),editTextPhone.text.toString())
            allPhone("")
            alertDialog.dismiss()
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
            var id: String,  var name: String,  var phoneNumber: String
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
            Log.d("txt", "x3")
            val data = list[position]
            holder.data = data
            holder.txtname.text = data.name
            holder.txtphone.text = data.phoneNumber
            holder.delete.setOnClickListener {
                deletePhone(data.phoneNumber,data.id)
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
            var delete:ImageButton = itemView.findViewById(R.id.deleteimgbuttom)

        }
    }

    @SuppressLint("Range")
    fun allPhone(text:String){
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

    fun deletePhone(phone :String,idp: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("ต้องการจะลบ เบอร์ $phone หรือไม่?")
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
}