package com.example.phoneblockerproject.example

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.phoneblockerproject.R

import android.app.KeyguardManager
import android.content.Context
import java.io.File


class TestrootFragment : Fragment() {

    var a:TextView?=null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_testroot, container, false)
         a = root.findViewById(R.id.rrr)
        isUsbDebuging()
        Log.d("isDevMode",isDevMode().toString())
        Log.d("isUsbDebuging",isUsbDebuging().toString())
        Log.d("isDeviceLocked",isDeviceLocked().toString())
        Log.d("isRooted",isRooted().toString())


        return root
    }

    // true = เปิด
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun isDevMode(): Boolean {
        return when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN -> {
                Settings.Secure.getInt(requireContext().contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN -> {
                @Suppress("DEPRECATION")
                Settings.Secure.getInt(requireContext().contentResolver,
                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
            }
            else -> false
        }
    }
        // true = เปิด
    fun isUsbDebuging():Boolean{
        val adb = Settings.Secure.getInt(requireContext().contentResolver, Settings.Secure.ADB_ENABLED, 0)
        return adb ==1
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun isDeviceLocked(): Boolean {
        val myKM = requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return myKM.isDeviceSecure
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
        val androidId = Settings.Secure.getString(context.contentResolver, "android_id")
        return "sdk" == Build.PRODUCT || "google_sdk" == Build.PRODUCT || androidId == null
    }



}