package de.pfiva.mobile.voiceassistant.utilities;

import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MobileVoiceAssistantUtilities {

    private static final String TAG = "VoiceAssitantUtils";

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.i(TAG, "Error while getting mac address of the device.");
        }
        return "02:00:00:00:00:00";
    }
}
