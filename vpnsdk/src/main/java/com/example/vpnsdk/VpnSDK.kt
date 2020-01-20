package com.example.vpnsdk

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import android.os.RemoteException
import androidx.annotation.NonNull
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread
import de.blinkt.openvpn.core.VpnStatus
import java.io.IOException

/**
 * VPN SDK to interface with OpenVPN library. It provides methods to connect and disconnect
 * the VPN connection. It also broadcasts the change in VPN connection.
 *
 * @author: Yousuf Sohail
 */
class VpnSDK private constructor(@NonNull val context: Context) {

    private var isServerStarted = false

    init {
        VpnStatus.initLogCache(context.cacheDir)
    }

    /**
     * Companion object which inherits from singleton holder class and receives constructor as lamda
     */
    companion object : SingletonHolder<VpnSDK, Context>(::VpnSDK) {
        const val EXTRA_KEY_STATUS = "status"
        const val ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST =
            "com.example.vpnsdk.broadcast.VPN_CONNECTION_CHANGED"
    }

    /**
     * Create the VPN connection
     */
    fun connect(): Boolean {
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(broadcastReceiver, IntentFilter("connectionState"))
        startVpn()
        return true
    }

    /**
     * Disconnect the VPN connect
     */
    fun disconnect(): Boolean {
        return stopVpn()
    }

    /**
     * Get the current status of VPN connection
     */
    fun getStatus(): String {
        return OpenVPNService.getStatus()
    }

    /**
     * Send a broadcast to signal the registered receivers that the VPN connection is change.
     * Either connected or disconnected
     *
     * @param context: Context
     * @param status: String
     */
    fun broadcastVpnConnectionStatusChange(@NonNull context: Context, status: String) {
        Intent().also { intent ->
            intent.action = ACTION_KEY_VPN_CONNECTION_CHANGED_BROADCAST
            intent.putExtra(EXTRA_KEY_STATUS, status)
            context.sendBroadcast(intent)
        }
    }

    private fun startVpn() {
        // check if
        if (!isServerStarted) {
            //checking permission for network monitor
            val intent = VpnService.prepare(context)
            if (intent != null && context is Activity) {
                context.startActivityForResult(intent, 1)
            } else {
                try {
                    val content = context.assets.open("au2-udp.ovpn").bufferedReader().use {
                        it.readText()
                    }
                    OpenVpnApi.startVpn(context, content, "ivacy0s6809988", "qwertyuiop")
                    isServerStarted = true
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Stop vpn
     *
     * @return boolean
     */
    private fun stopVpn(): Boolean {
        try {
            OpenVPNThread.stop()
            isServerStarted = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getStringExtra("state")
            if (state != null) broadcastVpnConnectionStatusChange(context, state)
        }
    }
}
