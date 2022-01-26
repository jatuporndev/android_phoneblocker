package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.R



class HomeFragment : Fragment() {
    var conphone:ConstraintLayout?=null
    var conmessage:ConstraintLayout?=null
    var imgsettings:ImageView?=null
    var conwifi:ConstraintLayout?=null
    var consecurity:ConstraintLayout?=null
    var memberID:String?=null
    var userstatus: String? = null
    var packageStatus:String?=null
    var imgpk1: ImageView? = null
    var imgpk2: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)

        packageStatus = sharedPrefer?.getString(LoginActivity().pac, null)
        userstatus = sharedPrefer?.getString(LoginActivity().userstatus, null)
        memberID = sharedPrefer?.getString(LoginActivity().memberIdPreference, null)



        val root = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as MainActivity).setTransparentStatusBar(1)
        imgsettings = root.findViewById(R.id.imgsettings)
        conphone = root.findViewById(R.id.conphone)
        conmessage = root.findViewById(R.id.conmessage)
        conwifi=root.findViewById(R.id.conwifi)
        consecurity=root.findViewById(R.id.consecurity)
        imgpk1 = root.findViewById(R.id.imgpk1)
        imgpk2 = root.findViewById(R.id.imgpk2)

        conphone?.isEnabled = false
        conmessage?.isEnabled = false

        Log.d("sdfsd","pac$packageStatus")
        Log.d("sdfsd","u$userstatus")

        if(userstatus == "true" && packageStatus =="1"){
            imgpk1?.visibility = View.GONE
            imgpk2?.visibility = View.GONE
            conphone?.isEnabled = true
            conmessage?.isEnabled = true
        }

        //รักษาความปลอดภัย
        consecurity?.setOnClickListener {
            val fragmentTransient = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransient.replace(R.id.nav_host_fragment,SecurityFragment())
            fragmentTransient.addToBackStack(null)
            fragmentTransient.commit()

        }
        //ไวไฟ
        conwifi?.setOnClickListener {
            val fragmentTransient = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransient.replace(R.id.nav_host_fragment,WifiFragment())
            fragmentTransient.addToBackStack(null)
            fragmentTransient.commit()

        }
        //บล็อกข้อความ
        conmessage?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, BlockerMessageFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        //บล็อกโทรศัพท์
        conphone?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, PhoneFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        //ผู้ใช้
        imgsettings?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, UsersFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return root
    }

    }
