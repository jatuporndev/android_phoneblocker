package com.example.phoneblockerproject

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.supervisorScope


class PhoneFragment : Fragment() {

    var imgaddphone:ImageView?=null
    var imgback:ImageButton?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =inflater.inflate(R.layout.fragment_phone, container, false)

        imgaddphone = root.findViewById(R.id.imgaddphone)
        imgaddphone?.setOnClickListener(){
            showCustomDialog()
        }

        imgback = root.findViewById(R.id.imgback)
        imgback?.setOnClickListener(){
            val fragmentTransaction = requireActivity().
            supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return root
    }


    private lateinit var alertDialog: AlertDialog
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.fragment_add_phone, null)

        /*val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val btnConfirm: Button = dialogView.findViewById(R.id.btnConfirm)
        btnBlock.setOnClickListener {
            //perform custom action
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }*/
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {

            }
        })
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.show()
    }

}