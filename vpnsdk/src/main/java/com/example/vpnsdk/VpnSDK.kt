package com.example.vpnsdk

import android.content.Context
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VPNLaunchHelper

object VpnSDK {

    fun connect(context: Context): Boolean {
        val profile = ProfileManager.getInstance(context).getProfileByName(vpnName)
        VPNLaunchHelper.startOpenVpn(profile, context)
        return true

    }

    fun disconnect(): Boolean {
        return true
    }

}