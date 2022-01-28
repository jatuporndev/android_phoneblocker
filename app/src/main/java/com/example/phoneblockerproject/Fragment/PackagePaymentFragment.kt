package com.example.phoneblockerproject.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.example.phoneblockerproject.Activity.LoginActivity
import com.example.phoneblockerproject.R

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
            var pac="m"
            if (radioButtonM?.isChecked == true){
                pac="1M"
            }else if(radioButtonY?.isChecked == true){
                pac="1Y"
            }

        }

        txtstatusPac?.visibility = View.GONE
        if (packageStatus=="1"){
            txtstatusPac?.visibility = View.VISIBLE
            btnregis?.visibility = View.GONE
        }


        return root
    }

}