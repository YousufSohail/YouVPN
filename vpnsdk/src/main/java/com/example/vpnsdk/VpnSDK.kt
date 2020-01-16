package com.example.vpnsdk

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.ProfileManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * VPN SDK to interface with OpenVPN library. It provides methods to connect and disconnect
 * the VPN connection. It also broadcasts the change in VPN connection.
 *
 * @author: Yousuf Sohail
 */
class VpnSDK private constructor(@NonNull val context: Context) {

    /**
     * Companion object which inherits from singleton holder class and receives constructor as lamda
     */
    companion object : SingletonHolder<VpnSDK, Context>(::VpnSDK) {
        const val EXTRA_KEY_CONNECTED = "connected"
        const val ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST =
            "com.example.vpnsdk.broadcast.VPN_CONNECTION_CHANGED"
    }

    /**
     * Create the VPN connection
     */
    fun connect(): Boolean {
        try {
            val content = context.assets.open("au2-udp.ovpn").bufferedReader().use {
                it.readText()
            }
            val profile = VpnProfile(content)
            startVPN(profile)

        } catch (e: IOException) {
            //log the exception
            return false
        }
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

    private fun startVPN(profile: VpnProfile) {

        ProfileManager.getInstance(context).saveProfile(context, profile)

        val intent = Intent(context, LaunchVPN::class.java)
        intent.putExtra(LaunchVPN.EXTRA_KEY, profile.uuid.toString())
        intent.action = Intent.ACTION_MAIN
        context.startActivity(intent)
    }

}
