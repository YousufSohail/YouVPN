package com.example.youvpn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.vpnsdk.VpnSDK

class MainActivity : AppCompatActivity() {

    private lateinit var vpnSDK: VpnSDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vpnSDK = VpnSDK.getInstance(this)
    }

    fun connect(view: View) {
        vpnSDK.connect()
    }

    fun disconnect(view: View) {
        vpnSDK.disconnect()
    }

}
