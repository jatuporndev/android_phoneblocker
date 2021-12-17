package com.example.phoneblockerproject.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phoneblockerproject.R
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import androidx.core.content.ContextCompat
import android.net.wifi.WifiInfo

import android.content.Context.WIFI_SERVICE

import androidx.core.content.ContextCompat.getSystemService
import android.content.Intent

import android.content.BroadcastReceiver
import android.net.wifi.ScanResult
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.LinkAddress
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.util.*

@RequiresApi(Build.VERSION_CODES.R)
class WifiFragment : Fragment() {
    var txtssid : TextView?=null
    var txthidden: TextView?=null
    var txtsignal: TextView?=null
    var txtfrequency: TextView?=null
    var txtlink: TextView?=null
    var txtip: TextView?=null
    var txtnetwork: TextView?=null
    var txtgateway: TextView?=null
    var txtdns: TextView?=null
    var txtdomains: TextView?=null
    var txthttp: TextView?=null
    var txtnetworkmet: TextView?=null
    var txtipv6: TextView?=null
    var conwifi:ConstraintLayout?=null

    var back:ImageView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =inflater.inflate(R.layout.fragment_wifi, container, false)
        txtssid=root.findViewById(R.id.txtssid)
        txthidden=root.findViewById(R.id.txthidden)
        txtsignal=root.findViewById(R.id.txtsignal)
        txtfrequency=root.findViewById(R.id.txtfrequency)
        txtlink=root.findViewById(R.id.txtlink)
        txtip=root.findViewById(R.id.txtip)
        txtnetwork=root.findViewById(R.id.txtnetwork)
        txtgateway=root.findViewById(R.id.txtgateway)
        txtdns=root.findViewById(R.id.txtdns)
        txtdomains=root.findViewById(R.id.txtdomains)
        txthttp=root.findViewById(R.id.txthttp)
        txtnetworkmet=root.findViewById(R.id.txtnetworkmet)
        txtipv6=root.findViewById(R.id.txtipv6)
        conwifi=root.findViewById(R.id.conwifi)
        conwifi?.visibility=View.GONE

        back=root.findViewById(R.id.imageView14)
        back?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager
            fragmentTransaction.popBackStack()
        }

        if(checkWifi()){
            onReviewPermissions()
        }


        return root
    }


    @SuppressLint("SetTextI18n")
    fun wifiinfo(){
        val wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        var wifiinfo: WifiInfo? = wifiManager.connectionInfo
        val connMan = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var wifiNet: Network? = null

        connMan.allNetworks.forEach {
            val caps = connMan.getNetworkCapabilities(it)
            if (caps != null && caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val netInfo = connMan.getNetworkInfo(it)
                if (netInfo != null && netInfo.isConnected) {
                    wifiNet = it
                }
            }
        }
        wifiNet!! == connMan.activeNetwork && connMan.isActiveNetworkMetered
        val linkProps = connMan.getLinkProperties(wifiNet)
        val defaultRoute = linkProps!!.routes.findLast { it.isDefaultRoute }

        //data here
        val ssid = wifiinfo!!.ssid.toString().drop(1).dropLast(1)
        val hiden_ssidd =wifiinfo.hiddenSSID.toString()
        val signal_level = WifiManager.calculateSignalLevel(wifiinfo.rssi,10).toString()+"/10";
        val Frequency = wifiinfo.frequency.toString()
        val linkSpeed = wifiinfo.linkSpeed.toString()
        val ipAddress = Formatter.formatIpAddress(wifiinfo.ipAddress)
        val network_mask= getNetmask(linkProps?.linkAddresses!!.first { it.address is Inet4Address })
        val gatewayAddress = defaultRoute!!.gateway
        val dnsServers = linkProps.dnsServers
        val domains =
            if (linkProps.domains.isNullOrBlank()) Collections.emptyList<String>()
            else linkProps!!.domains!!.split(delimiters = *charArrayOf(','))
        val httpProxy = linkProps.httpProxy.toString()
        val networkMetered= (wifiNet!! == connMan.activeNetwork && connMan.isActiveNetworkMetered).toString()

        val ipv6 = linkProps.linkAddresses.map { it.address }.filter { it is Inet6Address }
            .sortedWith(InetAddressComparator).joinToString(separator = "\n") { it.format() }

        //ดูส่วนนี้ แล้วนำไปโชว์
      //  Log.d("ssid", ssid.toString())
        //Log.d("hiden_ssidd", hiden_ssidd)
        //Log.d("signal_level", signal_level)
        // Log.d("Frequency", Frequency)
        //Log.d("linkSpeed", linkSpeed)
        // Log.d("ipAddress", ipAddress)
        // Log.d("network_mask", network_mask)
        //Log.d("gatewayAddress", gatewayAddress)
        // Log.d("dnsServers", dnsServers.toString())
        txtssid?.text=ssid
        if (hiden_ssidd == "false"){ txthidden?.text="No"}else{ txthidden?.text="Yes"}
        txtsignal?.text=signal_level
        txtfrequency?.text= "$Frequency MHz"
        txtlink?.text= "$linkSpeed Mbps"
        txtip?.text=ipAddress
        txtnetwork?.text=network_mask.hostAddress
        txtgateway?.text=gatewayAddress!!.hostAddress
      //  txtdns?.text=dnsServers.
        if (dnsServers.size>1){txtdns?.text=dnsServers[0].hostAddress +"\n"+ dnsServers[1].hostAddress }
        else{txtdns?.text=dnsServers[0].hostAddress }

        if (domains.isNullOrEmpty()){
            txtdomains?.text="None"
        }else{
            txtdomains?.text=domains[0]
        }

        if(httpProxy=="null"){
            txthttp?.text="None"
        }else{
            txthttp?.text=httpProxy
        }


        if (networkMetered=="false"){txtnetworkmet?.text="No"}else{ txtnetworkmet?.text="Yes"}
       // Log.d("httpProxy", httpProxy)
       // Log.d("networkMetered", networkMetered)
        txtipv6?.text=ipv6
       // Log.d("ipv6", ipv6)
    }
    private fun onReviewPermissions() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200)
        }else{
            conwifi?.visibility=View.VISIBLE
            wifiinfo()
        }
        }
    private fun getNetmask(la: LinkAddress): InetAddress {
        val prefixLength = la.prefixLength
        return Integer.reverseBytes((0xffffffff shl (32 - prefixLength)).toInt()).toInetAddress()
    }

    private fun Int.toInetAddress() =
        InetAddress.getByName(
            String.format("%d.%d.%d.%d",
                (this and 0xff),
                (this shr 8 and 0xff),
                (this shr 16 and 0xff),
                (this shr 24 and 0xff)
            ))

    private fun InetAddress.format() = this.hostAddress

    private object InetAddressComparator : Comparator<InetAddress> {
        override fun compare(o1: InetAddress?, o2: InetAddress?): Int {
            if (o1 is Inet4Address && o2 is Inet4Address ||
                o1 is Inet6Address && o2 is Inet6Address) {
                return o1.hostAddress.compareTo(o2.hostAddress)
            }
            if (o1 is Inet4Address && o2 is Inet6Address) {
                return -1
            }
            return 1
        }
    }

    fun checkWifi():Boolean{
        val wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        Log.d("wifi",wifiManager.startScan().toString())
        if(!wifiManager.isWifiEnabled){
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Wifi ยังไม่ได้เปิด ต้องการเปิดหรือไหม่?")
                .setCancelable(false)
                .setPositiveButton("เปิด") { _, _ ->
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    startActivity(intent)
                    val fragmentTransaction = requireActivity().supportFragmentManager
                    fragmentTransaction.popBackStack()

                }
                .setNegativeButton("ยกเลิก") { dialog, _ ->
                    dialog.cancel()
                    val fragmentTransaction = requireActivity().supportFragmentManager
                    fragmentTransaction.popBackStack()
                }
            val alert = dialogBuilder.create()
            alert.setTitle("AlertDialogWifi")
            alert.show()
        }

        return wifiManager.isWifiEnabled
    }

}