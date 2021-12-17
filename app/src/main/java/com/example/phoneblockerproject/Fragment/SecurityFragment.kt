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
import java.io.File


class SecurityFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_security, container, false)

        Log.d("root",isRooted().toString())
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