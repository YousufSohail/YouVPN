package com.example.youvpn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.vpnsdk.VpnSDK

/**
 * Receiver to listen the broadcast signalling the VPN connection change. Either connected or disconnected
 */
class VpnConnectionChangedBroadcastReceiver : BroadcastReceiver() {

    private val TAG = VpnConnectionChangedBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("Connected: ${intent.extras?.getBoolean(VpnSDK.EXTRA_KEY_CONNECTED)}\n")
            toString().also { log ->
                Log.d(TAG, log)
                Toast.makeText(context, log, Toast.LENGTH_LONG).show()
            }
        }
    }
}
