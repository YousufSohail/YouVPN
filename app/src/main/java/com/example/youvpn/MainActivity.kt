package com.example.youvpn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.vpnsdk.VpnSDK

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun connect(view: View) {
        VpnSDK.connect()
    }

    fun disconnect(view: View) {
        VpnSDK.disconnect()
    }

}
