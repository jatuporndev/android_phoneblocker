package com.example.phoneblockerproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.databass.DBHelper


class HomeFragment : Fragment() {

    var conphone: ConstraintLayout? = null
    var conmess: ConstraintLayout? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        conphone = root.findViewById(R.id.conphone)
        conphone?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, PhoneFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return root
    }
}
