package com.rubird.muzeipinterest;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by varunoberoi on 22/03/15.
 */
public class Utils {

    /**
     * Determines if the WIFI is connected
     * @param context the needed Context
     * @return true if connected
     */
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return  mWifi.isConnected();
    }

    /**
     * Determines if the INTERNET is connected
     * @param context the needed Context
     * @return true if connected
     */
    public static boolean isConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
