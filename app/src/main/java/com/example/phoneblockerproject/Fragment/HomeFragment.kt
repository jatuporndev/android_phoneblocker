package com.example.phoneblockerproject.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.R



class HomeFragment : Fragment() {
    var conphone:ConstraintLayout?=null
    var conmessage:ConstraintLayout?=null
    var imgsettings:ImageView?=null
    var conwifi:ConstraintLayout?=null
    var consecurity:ConstraintLayout?=null

    @RequiresApi(Build.VERSION_CODES.R)
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
        conmessage = root.findViewById(R.id.conmessage)
        conmessage?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, BlockerMessageFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        conwifi=root.findViewById(R.id.conwifi)
        conwifi?.setOnClickListener {
            val fragmentTransient = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransient.replace(R.id.nav_host_fragment,WifiFragment())
            fragmentTransient.addToBackStack(null)
            fragmentTransient.commit()

        }
        consecurity=root.findViewById(R.id.consecurity)
        consecurity?.setOnClickListener {
            val fragmentTransient = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransient.replace(R.id.nav_host_fragment,SecurityFragment())
            fragmentTransient.addToBackStack(null)
            fragmentTransient.commit()

        }
        return root
    }

    }
