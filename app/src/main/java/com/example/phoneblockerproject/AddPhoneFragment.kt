package com.example.phoneblockerproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AddPhoneFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    val data = ArrayList<Data>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_add_phone, container, false)
        data.add(Data("1","โจร","0222222"))
        data.add(Data("2","โจร2","0333333"))


        recyclerView = root.findViewById(R.id.recyclerView)
        //showDataList()
        return root
    }
    class Data(
            var id: String,  var name: String,  var phoneNumber: String
    )


}