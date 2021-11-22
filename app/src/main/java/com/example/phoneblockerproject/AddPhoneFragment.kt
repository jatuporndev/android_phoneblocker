package com.example.phoneblockerproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class AddPhoneFragment : Fragment() {

    val data = ArrayList<Data>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_add_phone, container, false)
        data.add(Data("1","โจร","0222222"))
        data.add(Data("2","โจร2","0333333"))

        return root
    }
    class Data(
            var id: String,  var name: String,  var phoneNumber: String
    )
}