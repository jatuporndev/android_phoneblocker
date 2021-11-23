package com.example.phoneblockerproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class HistoryFragment : Fragment() {

    val data = ArrayList<Data>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_history, container, false)
        data.add((Data("1","โจร","5555","0")))

        return root
    }
    class Data(
            var id: String,  var name: String,  var phoneNumber: String, var statsu: String
    )



}