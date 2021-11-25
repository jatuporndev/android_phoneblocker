package com.example.phoneblockerproject.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneblockerproject.R
import com.example.phoneblockerproject.databass.DBHelper
import kotlin.collections.ArrayList


class HistoryPhoneFragment : Fragment() {
    var imgbackc: ImageView? = null
    var data = ArrayList<Data>()
    var recyclerView: RecyclerView? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_history_phone, container, false)
        recyclerView = root.findViewById(R.id.recyclerView)
        allHistory()
        imgbackc = root.findViewById(R.id.imgbackc)
        imgbackc?.setOnClickListener {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HistoryFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        return root


    }
    class Data(
            var id: String,
            var name: String,
            var phonenember: String,
            var date: String,
            var status: String
    )
    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_blockphone,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = list[position]
            holder.data = data
            holder.txtname.text = data.name
            holder.txtphone.text = data.phonenember
            holder.txtdate.text = "บล็อกเมื่อ "+ data.date


        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var txtname: TextView = itemView.findViewById(R.id.txtname)
            var txtphone: TextView = itemView.findViewById(R.id.txtphone)
            var txtdate: TextView = itemView.findViewById(R.id.txtdate)

        }
    }
    @SuppressLint("Range")
    fun allHistory(){
        data.clear()
        val db = DBHelper(requireContext())
        val cur =db.gethistory("0")
        while (cur!!.moveToNext()) {
            data.add(Data(cur.getString(0), cur.getString(1), cur.getString(2),cur.getString(3),cur.getString(4)))
        }
        recyclerView!!.adapter = DataAdapter(data)
    }
}