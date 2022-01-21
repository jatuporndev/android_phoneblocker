package com.example.phoneblockerproject.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phoneblockerproject.MainActivity
import com.example.phoneblockerproject.R


class UsersFragment : Fragment() {

    var consignin: ConstraintLayout?=null
    var conpackage: ConstraintLayout?=null
    var condefense: ConstraintLayout?=null
    var conterms: ConstraintLayout?=null
    var consafety: ConstraintLayout?=null
    var imgback: ImageView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_users, container, false)

        imgback = root.findViewById(R.id.imgback)
        imgback?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }

        consignin = root.findViewById(R.id.consignin)
        consignin?.setOnClickListener{
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        conpackage = root.findViewById(R.id.conpackage)
        conpackage?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, PackageFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        condefense = root.findViewById(R.id.condefense)
        condefense?.setOnClickListener{
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, SettingsFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        conterms = root.findViewById(R.id.conterms)

        consafety = root.findViewById(R.id.consafety)
        consafety?.setOnClickListener {
            val fragmentTransient = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransient.replace(R.id.nav_host_fragment,SecurityFragment())
            fragmentTransient.addToBackStack(null)
            fragmentTransient.commit()

        }




        return root
    }


}