package io.shace.app.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.toolbox.NetworkImageView;

import io.shace.app.App;
import io.shace.app.R;
import io.shace.app.api.network.ApiImageLoader;

/**
 * Created by melvin on 4/30/14.
 *
 * Generic class to wrap common calls to block of codes linked to the networks
 */
public class NetworkTools {
    /**
     * Check if the current device has a working internet connexion
     *
     * @return the state of the connection
     */
    static public boolean hasInternet() {
        Context context = App.getContext();

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }

    /**
     * Display a Toast containing the "Server Error" message
     */
    static public void sendServerError() {
        ToastTools.use().longToast(R.string.server_error);
    }

    /**
     * Display a Toast containing the "Server cannot be reached" message
     */
    static public void sendTimeOutError() {
        ToastTools.use().longToast(R.string.server_timeout);
    }

    /**
     * Download, cache, and display an image
     *
     * @param url
     * @param view
     */
    static public void attachImage(String url, NetworkImageView view) {
        view.setImageUrl(url, ApiImageLoader.get());
    }
}
