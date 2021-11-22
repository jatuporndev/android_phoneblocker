package com.example.phoneblockerproject

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.supervisorScope


class PhoneFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var imgaddphone:ImageView?=null
    var imgback:ImageButton?=null
    val data = ArrayList<Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =inflater.inflate(R.layout.fragment_phone, container, false)
        data.add(Data("1", "โจร", "0222222"))
        data.add(Data("2", "โจร2", "0333333"))

        imgaddphone = root.findViewById(R.id.imgaddphone)
        imgaddphone?.setOnClickListener(){
            showCustomDialog()
        }

        imgback = root.findViewById(R.id.imgback)
        imgback?.setOnClickListener(){
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = DataAdapter(data)
        return root
    }

    private lateinit var alertDialog: AlertDialog
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.fragment_add_phone, null)

        /*val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val btnConfirm: Button = dialogView.findViewById(R.id.btnConfirm)
        btnBlock.setOnClickListener {
            //perform custom action
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }*/
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {

            }
        })
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
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

        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var txtname: TextView = itemView.findViewById(R.id.txtname)
            var txtphone: TextView = itemView.findViewById(R.id.txtphone)

        }
    }
}