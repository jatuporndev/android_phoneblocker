package com.example.phoneblockerproject

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.databass.DBHelper


class HistoryFragment : Fragment() {
    val data = ArrayList<Data>()
    var recyclerView: RecyclerView? = null
    var imgback:ImageView?=null
    var btnhisphone:Button?=null
    var btnhismessage:Button?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_history, container, false)
        data.add((Data("1","โจร","5555","1","23/11/2564")))

        btnhisphone = root.findViewById(R.id.btnhisphone)
        btnhisphone?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HistoryPhoneFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        btnhismessage = root.findViewById(R.id.btnhismessage)
        btnhismessage?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HistoryMessageFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        imgback = root.findViewById(R.id.imgback)
        imgback?.setOnClickListener{
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




    class Data(
            var id: String,  var name: String,  var phoneNumber: String, var statsu: String, var date: String
    )

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
            holder.txtphone2.text = data.phoneNumber
            holder.txtdate.text = data.date
            holder.conmenu.setOnLongClickListener() {
                showCustomDialog()
                return@setOnLongClickListener true
            }
            if (data.statsu == "0") {
                holder.imgicon.visibility = View.VISIBLE
            }else holder.imgicon.visibility = View.GONE

            if(data.statsu == "1"){
                holder.imgicon2.visibility = View.VISIBLE
            }else holder.imgicon2.visibility = View.GONE


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
        }
    }

    private lateinit var alertDialog: AlertDialog
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popup_menu_history, null)


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

}