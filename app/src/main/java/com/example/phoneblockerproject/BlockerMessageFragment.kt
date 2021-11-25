package com.example.phoneblockerproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior

class BlockerMessageFragment : Fragment() {
    var data = ArrayList<Data>()
    var recyclerView: RecyclerView? = null
    var imgback: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_blocker_message, container, false)
        data.add((Data("1", "man", "0957739456", "ดีจ้า", "25/11/2564")))
        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView!!.adapter = DataAdapter(data)

        imgback = root.findViewById(R.id.imgback)
        imgback?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return root
    }

    class Data(
        var id: String,
        var name: String,
        var phonenember: String,
        var message: String,
        var date: String
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
            holder.txtdate.text = data.date
            holder.txtmessage.text = data.message

        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var data:Data? = null
            var txtname: TextView = itemView.findViewById(R.id.txtname)
            var txtphone: TextView = itemView.findViewById(R.id.txtphone)
            var txtmessage: TextView = itemView.findViewById(R.id.txtmessage)
            var txtdate: TextView = itemView.findViewById(R.id.txtdate)

        }
    }
}