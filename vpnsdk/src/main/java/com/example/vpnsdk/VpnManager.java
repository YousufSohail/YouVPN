package com.example.vpnsdk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import de.blinkt.openvpn.LaunchVPN;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;

public class VpnManager {

    InputStream inputStream;
    BufferedReader bufferedReader;
    ConfigParser cp;
    VpnProfile vp;
    ProfileManager pm;
    Thread thread;

    private void start_vpn(String VPNFile){
//        SharedPreferences sp_settings;
//        sp_settings = getSharedPreferences("daily_usage", 0);
//        long connection_today = sp_settings.getLong(TODAY + "_connections", 0);
//        long connection_total = sp_settings.getLong("total_connections", 0);
//        SharedPreferences.Editor editor = sp_settings.edit();
//        editor.putLong(TODAY + "_connections", connection_today + 1);
//        editor.putLong("total_connections", connection_total + 1);
//        editor.apply();
//
//        Bundle params = new Bundle();
//        params.putString("device_id", App.device_id);
//        params.putString("city", City);
//        mFirebaseAnalytics.logEvent("app_param_country", params);

        App.connection_status = 1;
        try {
            inputStream = null;
            bufferedReader = null;
            try {
                assert VPNFile != null;
                inputStream = new ByteArrayInputStream(VPNFile.getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
//                params = new Bundle();
//                params.putString("device_id", App.device_id);
//                params.putString("exception", "MA11" + e.toString());
//                mFirebaseAnalytics.logEvent("app_param_error", params);
            }

            try { // M8
                assert inputStream != null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream/*, Charset.forName("UTF-8")*/));
            } catch (Exception e) {
//                params = new Bundle();
//                params.putString("device_id", App.device_id);
//                params.putString("exception", "MA12" + e.toString());
//                mFirebaseAnalytics.logEvent("app_param_error", params);
            }

            cp = new ConfigParser();
            try {
                cp.parseConfig(bufferedReader);
            } catch (Exception e) {
//                params = new Bundle();
//                params.putString("device_id", App.device_id);
//                params.putString("exception", "MA13" + e.toString());
//                mFirebaseAnalytics.logEvent("app_param_error", params);
            }
            vp = cp.convertProfile();
            vp.mAllowedAppsVpnAreDisallowed = true;

            EncryptData En = new EncryptData();
            SharedPreferences AppValues = getSharedPreferences("app_values", 0);
            String AppDetailsValues = En.decrypt(AppValues.getString("app_details", "NA"));

            try {
                JSONObject json_response = new JSONObject(AppDetailsValues);
                JSONArray jsonArray = json_response.getJSONArray("blocked");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_object = jsonArray.getJSONObject(i);
                    vp.mAllowedAppsVpn.add(json_object.getString("app"));
                    Log.e("packages", json_object.getString("app"));
                }
            } catch (JSONException e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA14" + e.toString());
                mFirebaseAnalytics.logEvent("app_param_error", params);
            }


            try {
                vp.mName = Build.MODEL;
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA15" + e.toString());
                mFirebaseAnalytics.logEvent("app_param_error", params);
            }

            vp.mUsername = Data.FileUsername;
            vp.mPassword = Data.FilePassword;

            try {
                pm = ProfileManager.getInstance(MainActivity.this);
                pm.addProfile(vp);
                pm.saveProfileList(MainActivity.this);
                pm.saveProfile(MainActivity.this, vp);
                vp = pm.getProfileByName(Build.MODEL);
                Intent intent = new Intent(getApplicationContext(), LaunchVPN.class);
                intent.putExtra(LaunchVPN.EXTRA_KEY, vp.getUUID().toString());
                intent.setAction(Intent.ACTION_MAIN);
                startActivity(intent);
                App.isStart = false;
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA16" + e.toString());
                mFirebaseAnalytics.logEvent("app_param_error", params);
            }
        } catch (Exception e) {
            params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("exception", "MA17" + e.toString());
            mFirebaseAnalytics.logEvent("app_param_error", params);
        }
    }

    public void stop_vpn(){
        App.connection_status = 0;
        OpenVPNService.abortConnectionVPN = true;
        ProfileManager.setConntectedVpnProfileDisconnected(this);

        if (mService != null) {

            try {
                mService.stopVPN(false);
            } catch (RemoteException e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA18" + e.toString());
                mFirebaseAnalytics.logEvent("app_param_error", params);
            }

            try {
                pm = ProfileManager.getInstance(this);
                vp = pm.getProfileByName(Build.MODEL);
                pm.removeProfile(this, vp);
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA17" + e.toString());
                mFirebaseAnalytics.logEvent("app_param_error", params);
            }


        }
    }

}
