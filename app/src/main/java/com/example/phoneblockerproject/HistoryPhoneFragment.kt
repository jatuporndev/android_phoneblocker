package com.example.phoneblockerproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class HistoryPhoneFragment : Fragment() {
    var imgbackc: ImageView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_history_phone, container, false)

        imgbackc = root.findViewById(R.id.imgbackc)
        imgbackc?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HistoryFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        return root
    }

}