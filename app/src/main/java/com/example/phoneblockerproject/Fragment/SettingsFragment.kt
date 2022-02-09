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
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import com.example.phoneblockerproject.Activity.SplashScreenActivity
import com.example.phoneblockerproject.databass.DBHelper
import eu.acolombo.progressindicatorview.ProgressIndicatorView
import org.json.JSONArray
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class SettingsFragment : Fragment() {
    var imgbackc:ImageView?=null
    var btnupdate:Button?=null
    var progressBar: ProgressIndicatorView?=null
    var imglogo:ImageView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_settings, container, false)

        imgbackc = root.findViewById(R.id.imgbackc)
        btnupdate = root.findViewById(R.id.btnupdate)
        progressBar = root.findViewById(R.id.progressIndicatorView)
        imglogo= root.findViewById(R.id.imageView4)
        progressBar?.visibility = View.GONE

        imgbackc?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()

        }

        btnupdate?.setOnClickListener {
            val clk_rotate = AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.rotate_clockwise
            )
            imglogo?.startAnimation(clk_rotate)

            setstatus(true)
            addDataServer()
        }

        return  root
    }


    private fun addDataServer() {
        progressBar?.progress =0
        val db = DBHelper(requireContext())
        val url: String = getString(R.string.root_url) + getString(R.string.getphone_url)
        Log.d("Mainactivity",url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d("Mainactivity","x1")
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        db.deletePhoneSpamer()
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            db.addPhoneSpam(item.getString("name"),item.getString("phonenumber"))
                        }
                        addMailServer()// update ข้อความต่อ
                    }
                } catch (e: JSONException) {
                    Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
                    setstatus(false)
                }
            } else {
                Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
                setstatus(false)
            }
        } catch (e: IOException) {
            Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
            setstatus(false)
        }
    }
    private fun addMailServer() {
        progressBar?.progress =50
        val db = DBHelper(requireContext())
        val url: String = getString(R.string.root_url) + getString(R.string.getsms_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        db.deleteSMSSpamer()
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            db.addSmsSpam(item.getString("name"),item.getString("addressmessage"))
                        }
                        progressBar?.progress =100
                        progressBar?.listener?.onMaxReached = {
                            val intent = Intent(requireContext(), SplashScreenActivity::class.java)
                            requireActivity().startActivity(intent)
                            requireActivity().finishAffinity() }

                    }
                } catch (e: JSONException) {
                    Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
                    setstatus(false)
                }
            } else {
                Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
                setstatus(false)
            }
        } catch (e: IOException) {
            Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
            setstatus(false)
        }
    }
    fun setstatus(status:Boolean){
        if(status){
            progressBar?.visibility = View.VISIBLE
            btnupdate?.text = "กำลังอัปเดท"
            btnupdate?.isEnabled =false
        }else{
            progressBar?.visibility = View.GONE
            btnupdate?.text = "อัปเดทข้อมูล"
            btnupdate?.isEnabled =true
        }

    }

}