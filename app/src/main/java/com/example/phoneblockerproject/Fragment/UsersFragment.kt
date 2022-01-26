package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.Activity.LoginActivity
import com.example.phoneblockerproject.Activity.MainActivity
import com.example.phoneblockerproject.R
import okhttp3.OkHttpClient
import okhttp3.Request
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

        if (userstatus == "true") {
          consignin?.visibility = View.GONE
            txtusername?.text = username
            if (packstatus =="1"){
                dataMember()
            }


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

                        txtstatus?.text ="ทดลองใช้งานถึง "+ data.getString("exp_date")
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
            // ซ์้อแพ็กแก็จ
        conpackage?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, PackageFragment())
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

}