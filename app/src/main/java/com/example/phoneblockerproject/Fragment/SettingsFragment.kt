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
import android.widget.Toast
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.SplashScreenActivity
import com.example.phoneblockerproject.databass.DBHelper
import org.json.JSONArray
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class SettingsFragment : Fragment() {
    var imgbackc:ImageView?=null
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
            addDataServer()

        }

        return  root
    }


    private fun addDataServer() {
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

                        val intent = Intent(requireContext(), SplashScreenActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finishAffinity()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_LONG).show()
        }
    }


}