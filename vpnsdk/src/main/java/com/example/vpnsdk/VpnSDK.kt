package com.example.vpnsdk

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull

/**
 * VPN SDK to interface with OpenVPN library. It provides methods to connect and disconnect
 * the VPN connection. It also broadcasts the change in VPN connection.
 *
 * @author: Yousuf Sohail
 */
class VpnSDK private constructor(@NonNull val context: Context) {

    companion object : SingletonHolder<VpnSDK, Context>(::VpnSDK) {
        const val EXTRA_KEY_CONNECTED = "connected"
        const val ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST =
            "com.example.vpnsdk.broadcast.VPN_CONNECTION_CHANGED"
    }

    /**
     * Create the VPN connection
     */
    fun connect(): Boolean {
        return true

    }

    /**
     * Disconnect the VPN connect
     */
    fun disconnect(): Boolean {
        return true
    }

    /**
     * Send a broadcast to signal the registered receivers that the VPN connection is change.
     * Either connected or disconnected
     *
     * @param context: Context
     * @param isConnected: Boolean
     */
    fun VpnConnectionChangedBroadcast(@NonNull context: Context, isConnected: Boolean) {
        Intent().also { intent ->
            intent.action = ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST
            intent.putExtra(EXTRA_KEY_CONNECTED, isConnected)
            context.sendBroadcast(intent)
        }
    }

}
