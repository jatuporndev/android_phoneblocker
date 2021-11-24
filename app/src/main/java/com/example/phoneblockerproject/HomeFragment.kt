package com.example.phoneblockerproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout


class HomeFragment : Fragment() {
    var conphone:ConstraintLayout?=null
    var imgsettings:ImageView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        imgsettings = root.findViewById(R.id.imgsettings)
        imgsettings?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, SettingsFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        conphone = root.findViewById(R.id.conphone)
        conphone?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, PhoneFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return root
    }

    }
