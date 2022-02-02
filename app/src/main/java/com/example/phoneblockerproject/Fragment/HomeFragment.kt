package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.example.phoneblockerproject.Activity.LoginActivity
import com.example.phoneblockerproject.Activity.MainActivity
import com.example.phoneblockerproject.Detector.StatusDevice
import com.example.phoneblockerproject.R
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


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
        val isonline = StatusDevice().isOnline(requireContext())

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

        if(userstatus == "true" && packageStatus =="1" && isonline){
            imgpk1?.visibility = View.GONE
            imgpk2?.visibility = View.GONE
            conphone?.isEnabled = true
            conmessage?.isEnabled = true

           updateExp()
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

    fun updateExp(){
        var st=false
        var url: String = getString(R.string.root_url) + getString(R.string.updateExpurl)+memberID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        try {

                val response = okHttpClient.newCall(request).execute()
                if (response!!.isSuccessful) {
                    try {
                        val data = JSONObject(response.body!!.string())
                        if (data.length() > 0) {
                            if (data.getString("status") == "true") {
                                val sharedPrefer: SharedPreferences =
                                    requireActivity().getSharedPreferences(
                                        LoginActivity().appPreference,
                                        Context.MODE_PRIVATE
                                    )
                                val editor: SharedPreferences.Editor = sharedPrefer.edit()

                                editor.putString(LoginActivity().pac, "0")

                                editor.commit()
                                Toast.makeText(
                                    requireContext(),
                                    "วันใช้งานของคุณหมดอายุแล้ว",
                                    Toast.LENGTH_LONG
                                ).show()
                                imgpk1?.visibility = View.VISIBLE
                                imgpk2?.visibility = View.VISIBLE
                                conphone?.isEnabled = false
                                conmessage?.isEnabled = false

                            }

                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    response.code
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    }
