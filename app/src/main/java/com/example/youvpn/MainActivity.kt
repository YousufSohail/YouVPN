package com.example.youvpn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.vpnsdk.VpnSDK
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var vpnSDK: VpnSDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vpnSDK = VpnSDK.getInstance(this)
    }

    override fun onStart() {
        super.onStart()

        tvStatus.text = vpnSDK.getStatus()

        val filter = IntentFilter()
        filter.addAction(VpnSDK.ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST)
        registerReceiver(broadcastReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    fun connect(view: View) {
        vpnSDK.connect()
    }

    fun disconnect(view: View) {
        vpnSDK.disconnect()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context, intent: Intent) {
            tvStatus.text = intent.extras?.getString(VpnSDK.EXTRA_KEY_STATUS)
        }
    }

}
