package com.example.phoneblockerproject.Fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.phoneblockerproject.R
import com.example.phoneblockerproject.databass.DBHelper
import java.lang.Exception

@RequiresApi(Build.VERSION_CODES.R)
class ListwifiFragment : Fragment() {
    var data = ArrayList<Data>()
    var recyclerView:RecyclerView?=null
    var progressBar:ProgressBar?=null
    var swipe:SwipeRefreshLayout?=null
    var ssid:String=""
    var back:ImageView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root =  inflater.inflate(R.layout.fragment_listwifi, container, false)
        recyclerView = root.findViewById(R.id.recyclerView2)
        progressBar = root.findViewById(R.id.progressBar)
        swipe = root.findViewById(R.id.swipe)
        back=root.findViewById(R.id.back)

        back?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }

        val bundle = this.arguments
        ssid =bundle?.get("ssid").toString()

        swipe?.setOnRefreshListener {
            scan()
            swipe?.isRefreshing = false
        }

        scan()
        return root
    }



    class Data(
        var ssid: String,
        var BSSID: String,
        var capabilities: String,
        var siglevel:Int

    )
    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_listwifi,
                parent, false
            )
            return ViewHolder(view)
        }

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            holder.txtnamwifi.text = data.ssid
            if (data.ssid == ssid){
                holder.txtnamwifi.setTextColor(Color.parseColor("#FFC766"))
            }

            when(data.siglevel){
                1 -> holder.imgwifi.setImageResource(R.drawable.wifi1)
                2 -> holder.imgwifi.setImageResource(R.drawable.wifi2)
                3 -> holder.imgwifi.setImageResource(R.drawable.wifi3)
                4 -> holder.imgwifi.setImageResource(R.drawable.wifi4)
                else -> holder.imgwifi.setImageResource(R.drawable.wifi4)
            }

            if (data.capabilities.contains("none")){
                holder.imglock.visibility =View.GONE
            }
            holder.comwifi.setOnClickListener {
                addwifi(data.ssid,data.BSSID)
                val fragmentTransaction = requireActivity().supportFragmentManager
                fragmentTransaction.popBackStack()
            }


        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var data: Data? = null
            var txtnamwifi:TextView = itemView.findViewById(R.id.txtnamwifi)
            var imgwifi:ImageView = itemView.findViewById(R.id.imgwifi)
            var comwifi:ConstraintLayout = itemView.findViewById(R.id.conwifi)
            var imglock:ImageView = itemView.findViewById(R.id.imglock)
        }
    }

        fun addwifi(ssid: String,bssid:String){
            val db = DBHelper(requireContext())
            db.addwifi(ssid,bssid)
        }

        fun scan(){
            val wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            val wifiScanReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        scanSuccess()
                    } else {
                        scanFailure()
                    }
                }
            }

            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            requireContext().registerReceiver(wifiScanReceiver, intentFilter)

            val success = wifiManager.startScan()
            if (!success) {
                scanFailure()
            }

        }
    fun scanSuccess() {
        try{


        data.clear()
        val wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val results = wifiManager.scanResults

        results.forEach {
            if (!it.SSID.isNullOrBlank()){
                var lvls = wifiManager.calculateSignalLevel(it.level)
                    data.add((Data(it.SSID,it.BSSID,it.capabilities,lvls)))

            }
        }
        recyclerView!!.adapter = DataAdapter(data)
        progressBar?.visibility =View.GONE

        }catch (ex:Exception){

        }
    }
    fun scanFailure() {
        try{
        val wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults

        }catch (ex:Exception){

        }
    }

}