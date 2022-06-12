package com.shakib.etheriumchecker.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtils {

    // connection states ---------------------------------------------------------------------------
    public static final int TYPE_NOT_CONNECTED = 0;
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    private static final int NETWORK_STATUS_WIFI = 1;
    private static final int NETWORK_STATUS_MOBILE = 2;
    // ---------------------------------------------------------------------------------------------

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network network = cm.getActiveNetwork();
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                if (capabilities != null) {
                    if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        return TYPE_WIFI;
                    }
                    if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        return TYPE_MOBILE;
                    }
                }else{
                    return TYPE_NOT_CONNECTED;
                }
            } else {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork != null){
                    if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                        return TYPE_WIFI;
                    }
                    if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                        return TYPE_MOBILE;
                    }
                }else{
                    return TYPE_NOT_CONNECTED;
                }
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtils.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkUtils.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == NetworkUtils.TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        }
        return status;
    }

    public static boolean hasInternetConnection(Context context) {
        return NetworkUtils.getConnectivityStatusString(context) != NetworkUtils.NETWORK_STATUS_NOT_CONNECTED;
    }

    public static boolean isWifiConnected(Context context) {
        return NetworkUtils.getConnectivityStatusString(context) == NETWORK_STATUS_WIFI;
    }

    public static boolean isMobileNetworkConnected(Context context) {
        return NetworkUtils.getConnectivityStatusString(context) == NETWORK_STATUS_MOBILE;
    }
}
