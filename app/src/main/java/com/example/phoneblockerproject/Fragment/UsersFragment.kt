package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.Activity.LoginActivity
import com.example.phoneblockerproject.Activity.MainActivity
import com.example.phoneblockerproject.Detector.StatusDevice
import com.example.phoneblockerproject.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class UsersFragment : Fragment() {

    var consignin: ConstraintLayout?=null
    var conlogout: ConstraintLayout?=null
    var conpackage: ConstraintLayout?=null
    var condefense: ConstraintLayout?=null
    var conterms: ConstraintLayout?=null
    var consafety: ConstraintLayout?=null
    var imgback: ImageView?=null
    var memberID:String?=null
    var userstatus: String? = null
    var packstatus: String? = null
    var txtusername: TextView? = null
    var txtstatus:TextView?=null
    var free_tiral:String = "0"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userstatus = sharedPrefer?.getString(LoginActivity().userstatus, null)
        memberID = sharedPrefer?.getString(LoginActivity().memberIdPreference, null)
        packstatus = sharedPrefer?.getString(LoginActivity().pac, null)
        val username = sharedPrefer?.getString(LoginActivity().usernamePreference, null)
        val isonline = StatusDevice().isOnline(requireContext())
        (activity as MainActivity).setTransparentStatusBar(0)

        var root =  inflater.inflate(R.layout.fragment_users, container, false)
        txtusername = root.findViewById(R.id.txtusername)
        imgback = root.findViewById(R.id.imgback)
        consignin = root.findViewById(R.id.consignin)
        conpackage = root.findViewById(R.id.conpackage)
        condefense = root.findViewById(R.id.condefense)
        conterms = root.findViewById(R.id.conterms)
        consafety = root.findViewById(R.id.consafety)
        conlogout = root.findViewById(R.id.conlogout)
        txtstatus = root.findViewById(R.id.txtstatus)

        consignin?.visibility = View.VISIBLE

//        Log.d("twtwss",userstatus!!)

        if (userstatus == "true" && isonline) {
          consignin?.visibility = View.GONE
            dataMember()
        }else {
            conlogout?.visibility = View.GONE
        }
        viewmember()
        return root

    }

    fun dataMember(){
        var url: String = getString(R.string.root_url) + getString(R.string.viewmember_url) + memberID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        txtusername?.text = data.getString("username")
                        free_tiral = data.getString("free_trial")
                        if(data.getString("package")=="1") {
                            txtstatus?.text = "ทดลองใช้งานถึง " + data.getString("exp_date")
                            updateExp()
                        }else{
                            txtstatus?.text = "ฟรี"
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                response.code
                Toast.makeText(requireContext(), "เชื่อมต่อ serve ล้มเหลว", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewmember() {

            // ออกจากระบบ
        conlogout?.setOnClickListener {
            confrimDialogLogout()
        }
            // ตั้งค่าความปลอดภัย
        consafety?.setOnClickListener {
            val fragmentTransient = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransient.replace(R.id.nav_host_fragment,SecurityFragment())
            fragmentTransient.addToBackStack(null)
            fragmentTransient.commit()

        }
            // ตั้งค่า
        condefense?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, SettingsFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
            // ซ์้อแพ็กเกจ
        conpackage?.setOnClickListener{
            Log.d("test23","b"+free_tiral)
            var fm :Fragment = PackageFragment()
            fm = if(free_tiral == "0"){
                PackageFragment()
            }else{
                PackagePaymentFragment()
            }


            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fm)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
            // ล็อกอิน
        consignin?.setOnClickListener{
            startActivity(Intent(context, LoginActivity::class.java))
           // val fragmentTransaction = requireActivity().supportFragmentManager
           // fragmentTransaction.popBackStack()

        }
            // กลับ
        imgback?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }

    }

    fun confrimDialogLogout(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("ต้องการจะจากระะบบหรือไม่?")
            .setCancelable(false)
            .setPositiveButton("ใช่") { _, _ ->
                val sharePrefer = requireContext().getSharedPreferences(
                    LoginActivity().appPreference,
                    Context.MODE_PRIVATE
                )
                val editor = sharePrefer.edit()
                editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

                editor.commit() // ยืนยันการแก้ไข preferences
                //return to login page
                val fragmentTransaction = requireActivity().
                supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }
            .setNegativeButton("ยกเลิก") { dialog, id ->

                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

    }
    fun updateExp(){
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
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        if(data.getString("status") =="true"){
                            val sharedPrefer: SharedPreferences =
                                requireActivity().getSharedPreferences(LoginActivity().appPreference, Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPrefer.edit()

                            editor.putString(LoginActivity().pac, "0")

                            editor.commit()
                            Toast.makeText(requireContext(), "วันใช้งานของคุณหมดอายุแล้ว", Toast.LENGTH_LONG).show()

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