package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.example.phoneblockerproject.Activity.LoginActivity
import com.example.phoneblockerproject.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class PackagePaymentFragment : Fragment() {
    var imgback:ImageView?=null
    var btnregis:Button?=null
    var radioButtonM :RadioButton?=null
    var radioButtonY:RadioButton?=null
    var radioButtonGroup:RadioGroup?=null
    var txtstatusPac:TextView?=null

    var memberID:String?=null
    var packageStatus:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_package_payment, container, false)
        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)

        packageStatus = sharedPrefer?.getString(LoginActivity().pac, null)
        memberID = sharedPrefer?.getString(LoginActivity().memberIdPreference, null)

        imgback = root.findViewById(R.id.imgback3)
        btnregis = root.findViewById(R.id.btnre)
        radioButtonM = root.findViewById(R.id.radioButtonM)
        radioButtonY = root.findViewById(R.id.radioButtonY)
        radioButtonGroup = root.findViewById(R.id.radioGroup)
        txtstatusPac = root.findViewById(R.id.txtstatusPac)
        imgback?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }
        radioButtonM?.isChecked=true
        btnregis?.setOnClickListener {
            if (radioButtonM?.isChecked == true){
                update("1")
            }
            if(radioButtonY?.isChecked == true){
                update("0")
            }

        }

        txtstatusPac?.visibility = View.GONE
        if (packageStatus=="1"){
            txtstatusPac?.visibility = View.VISIBLE
            btnregis?.visibility = View.GONE
        }


        return root
    }

    fun update(f:String){
        var url: String = getString(R.string.root_url) + getString(R.string.updateExpPay_url)+memberID+"/"+f
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
                        Toast.makeText(requireContext(), "เริ่มใช้งานพรีเมียมแล้ว", Toast.LENGTH_LONG).show()
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