package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phoneblockerproject.R
import android.os.Build
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.File


class SecurityFragment : Fragment() {
    var imgback:ImageView?=null
    var txtmode: TextView?=null
    var txtusb: TextView?=null
    var txtroot: TextView?=null
    var txtlook: TextView?=null
    var conmode:ConstraintLayout?=null
    var conusb:ConstraintLayout?=null
    var conroot:ConstraintLayout?=null
    var conlook:ConstraintLayout?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_security, container, false)
        Log.d("root",isRooted().toString())

        imgback = root.findViewById(R.id.imgback)
                imgback?.setOnClickListener(){
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }


        txtmode = root.findViewById(R.id.txtmode)
        txtmode?.visibility = View.GONE
        conmode = root.findViewById(R.id.conmode)
        conmode?.setOnClickListener(){
            txtusb?.visibility = View.GONE
            txtroot?.visibility = View.GONE
            txtlook?.visibility = View.GONE
            txtmode?.visibility = View.VISIBLE
        }

        txtusb = root.findViewById(R.id.txtusb)
        txtusb?.visibility = View.GONE
        conusb = root.findViewById(R.id.conusb)
        conusb?.setOnClickListener(){
            txtmode?.visibility = View.GONE
            txtroot?.visibility = View.GONE
            txtlook?.visibility = View.GONE
            txtusb?.visibility = View.VISIBLE
        }

        txtroot = root.findViewById(R.id.txtroot)
        txtroot?.visibility = View.GONE
        conroot = root.findViewById(R.id.conroot)
        conroot?.setOnClickListener(){
            txtusb?.visibility = View.GONE
            txtmode?.visibility = View.GONE
            txtlook?.visibility = View.GONE
            txtroot?.visibility = View.VISIBLE
        }

        txtlook = root.findViewById(R.id.txtlook)
        txtlook?.visibility = View.GONE
        conlook = root.findViewById(R.id.conlook)
        conlook?.setOnClickListener(){
            txtroot?.visibility = View.GONE
            txtusb?.visibility = View.GONE
            txtmode?.visibility = View.GONE
            txtlook?.visibility = View.VISIBLE
        }



        return root
    }


    fun isRooted(): Boolean {
        val isEmulator: Boolean = isEmulator(requireContext())
        val buildTags = Build.TAGS
        return if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
            true
        } else {
            var file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                true
            } else {
                file = File("/system/xbin/su")
                !isEmulator && file.exists()
            }
        }
    }
    fun isEmulator(context: Context): Boolean {
        val androidId = Secure.getString(context.contentResolver, "android_id")
        return "sdk" == Build.PRODUCT || "google_sdk" == Build.PRODUCT || androidId == null
    }


}