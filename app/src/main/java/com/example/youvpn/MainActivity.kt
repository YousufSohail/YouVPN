package com.example.youvpn

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
        tvStatus.text = vpnSDK.getStatus()
    }

    fun connect(view: View) {
        vpnSDK.connect()
    }

    fun disconnect(view: View) {
        vpnSDK.disconnect()
    }

}
