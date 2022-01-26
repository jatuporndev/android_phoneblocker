package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class PackageFragment : Fragment() {
    var btnback:ImageView?=null
    var btntiral:Button?=null
    var btnlater:TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_package, container, false)
        (activity as MainActivity).setTransparentStatusBar(0)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        val memberID = sharedPrefer?.getString(LoginActivity().memberIdPreference, null)
        val userstatus = sharedPrefer?.getString(LoginActivity().userstatus, null)

        btnback = root.findViewById(R.id.imgBackPre)
        btntiral = root.findViewById(R.id.btntrial)
        btnlater = root.findViewById(R.id.btnlater)

        btnback?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }

        btnlater?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }
        btntiral?.setOnClickListener {
            if (userstatus =="true"){
                updateFree(memberID!!)
            }else{
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)

            }

        }

        return root

    }


    fun updateFree(id:String){
        var url: String = getString(R.string.root_url) + getString(R.string.updateFree_url)+id
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Toast.makeText(requireContext(), "เริ่มทดลองใช้ฟรีแล้ว", Toast.LENGTH_LONG).show()
                        val sharedPrefer: SharedPreferences =
                            requireActivity().getSharedPreferences(LoginActivity().appPreference, Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPrefer.edit()

                        editor.putString(LoginActivity().pac, "1")

                        editor.commit()

                        val fragmentTransaction = requireActivity().supportFragmentManager
                        fragmentTransaction.popBackStack()
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