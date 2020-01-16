package com.example.vpnsdk

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull

/**
 * VPN SDK to interface with OpenVPN library.
 *
 * @author: Yousuf Sohail
 */
class VpnSDK private constructor(@NonNull context: Context) {

    private val mContext: Context = context
    private val ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST = "com.example.vpnsdk.broadcast.VPN_CONNECTION_CHANGED"

    val EXTRA_KEY_CONNECTED = "connected"

    companion object : SingletonHolder<VpnSDK, Context>(::VpnSDK)

    /**
     * Create the VPN connection
     */
    fun connect(): Boolean {
        mContext
        return true

    }

    /**
     * Disconnect the VPN connect
     */
    fun disconnect(): Boolean {
        return true
    }

    /**
     * To signal the consuming app VPN connection change. Either connected or disconnected
     */
    fun VpnConnectionChangedBroadcast(@NonNull context: Context, isConnected: Boolean) {
        Intent().also { intent ->
            intent.action = ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST
            intent.putExtra(EXTRA_KEY_CONNECTED, isConnected)
            context.sendBroadcast(intent)
        }
    }
}
