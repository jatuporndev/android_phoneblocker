package com.example.phoneblockerproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.phoneblockerproject.R
import android.content.Intent
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.SplashScreenActivity


class SettingsFragment : Fragment() {
    var imgbackc:ImageView?=null
    var data = ArrayList<Data>()
    var btnupdate:Button?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_settings, container, false)

        imgbackc = root.findViewById(R.id.imgbackc)
        btnupdate = root.findViewById(R.id.btnupdate)
        imgbackc?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        btnupdate?.setOnClickListener {
            val intent = Intent(requireContext(), SplashScreenActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finishAffinity()
        }

        return  root
    }

    class Data(var id:String,var name:String,var phone:String)
    fun dataServer():ArrayList<Data>{
        var array = ArrayList<Data>()
      //  data.add(Data("1","test","name"))
        return array
    }



}