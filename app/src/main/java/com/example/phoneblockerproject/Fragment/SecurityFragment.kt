package com.example.phoneblockerproject.Fragment

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phoneblockerproject.R
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.io.File

import androidx.core.hardware.fingerprint.FingerprintManagerCompat

import android.hardware.fingerprint.FingerprintManager

import android.content.pm.PackageManager
import androidx.biometric.BiometricManager

import androidx.core.app.ActivityCompat





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
    //img
    var statusDevMode:ImageView?=null
    var statusUsbMode:ImageView?=null
    var statusRootMode:ImageView?=null
    var statusLockMode:ImageView?=null

    var txtdevmode:TextView?=null
    var txtusbmode:TextView?=null
    var txtrootmode:TextView?=null
    var txtlockmode:TextView?=null

    var txtpro:TextView?=null
    var chart: CircularProgressBar?=null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_security, container, false)
        handle(root)


        imgback?.setOnClickListener(){
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }

        txtmode?.visibility = View.GONE
        conmode?.setOnClickListener(){
            if(txtmode?.visibility == View.VISIBLE){
                txtmode?.visibility = View.GONE
            }else{
                txtusb?.visibility = View.GONE
                txtroot?.visibility = View.GONE
                txtlook?.visibility = View.GONE
                txtmode?.visibility = View.VISIBLE
            }

        }

        txtusb?.visibility = View.GONE
        conusb?.setOnClickListener(){
            if(txtusb?.visibility == View.VISIBLE){
                txtusb?.visibility = View.GONE
            }else{
                txtmode?.visibility = View.GONE
                txtroot?.visibility = View.GONE
                txtlook?.visibility = View.GONE
                txtusb?.visibility = View.VISIBLE
            }



        }

        txtroot?.visibility = View.GONE

      /*  conroot?.setOnClickListener(){
            txtusb?.visibility = View.GONE
            txtmode?.visibility = View.GONE
            txtlook?.visibility = View.GONE
            txtroot?.visibility = View.VISIBLE
        }
*/
        txtlook?.visibility = View.GONE
        conlook?.setOnClickListener(){
            if(  txtlook?.visibility == View.VISIBLE){
                txtlook?.visibility = View.GONE
            }else{
                txtroot?.visibility = View.GONE
                txtusb?.visibility = View.GONE
                txtmode?.visibility = View.GONE
                txtlook?.visibility = View.VISIBLE
            }
        }

        Log.d("test",isFingerprint().toString())

        check()
        setting()
        return root
    }
    fun handle(root:View){
        statusDevMode=root.findViewById(R.id.statusdevmode)
        statusUsbMode=root.findViewById(R.id.statususbmode)
        statusRootMode=root.findViewById(R.id.statusrootmode)
        statusLockMode=root.findViewById(R.id.statuslock)

        txtdevmode = root.findViewById(R.id.txtdevmode)
        txtusbmode=root.findViewById(R.id.txtusbmode)
        txtrootmode = root.findViewById(R.id.txtrottmode)
        txtlockmode = root.findViewById(R.id.txtlockmode)

        chart=root.findViewById(R.id.circularProgressBar)
        txtpro = root.findViewById(R.id.txtpro)
        imgback = root.findViewById(R.id.imgback)

        conmode = root.findViewById(R.id.conmode)
        txtmode = root.findViewById(R.id.txtmode)
        conusb = root.findViewById(R.id.conusb)
        txtusb = root.findViewById(R.id.txtusb)
        conroot = root.findViewById(R.id.conroot)
        txtroot = root.findViewById(R.id.txtroot)
        conlook = root.findViewById(R.id.conlook)
        txtlook = root.findViewById(R.id.txtlook)
    }

    fun chartprocess(pro:Float){
        txtpro?.text="${pro.toInt()}/4"
        chart?.apply {
            // Set Progress
            progress = 0f
            // or with animation
            setProgressWithAnimation(pro, 1000) // =1s

            // Set Progress Max
            progressMax = 4f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.RED
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.RED
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun check(){
        var i = 0f
        if (!isDevMode()){
            statusDevMode?.setImageResource(R.drawable.correct)
            txtdevmode?.text="ปิดโหมดผู้พัฒนาแล้ว ปลอดภัยแล้ว!! "
            i+=1
        }
        if(!isUsbDebuging()){
            statusUsbMode?.setImageResource(R.drawable.correct)
            txtusbmode?.text="ปิดการแก้ไขข้อบกพร่องUSBแล้ว ปลอดภัยแล้ว!! "
            i+=1
        }
        if(!isRooted()){
            statusRootMode?.setImageResource(R.drawable.correct)
            txtrootmode?.text="ไม่มีการรูท ปลอดภัยแล้ว!! "
            i+=1
        }
        if(isDeviceLocked()){
            statusLockMode?.setImageResource(R.drawable.correct)
            txtlockmode?.text="ล็อคหน้าจอแล้ว ปลอดภัยแล้ว!! "
            i+=1
        }
        chartprocess(i)
    }

    fun setting(){
        txtmode?.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            startActivity(intent)
        }
        txtusb?.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            startActivity(intent)
        }
        txtlook?.setOnClickListener {
            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            startActivity(intent)

        }
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

    fun isFingerprint():Boolean {
        return BiometricManager.from(requireContext()).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS

    }



}