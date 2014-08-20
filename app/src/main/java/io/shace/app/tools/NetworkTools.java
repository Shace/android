package io.shace.app.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import io.shace.app.App;
import io.shace.app.R;

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
     * Validate basic forms through an API call
     *
     * TODO: Proper documentation
     * TODO: Adapt to shace
     *
     *
     * @param tag Tag of the class
     * @param fields Hashmap containing the fields of the form. The key must match the post name of the data
     * @param errorCode Error code sent by the server
     * @param response Response of the server
     */
    static public void validateBasicApiForm(String tag, HashMap<String, TextView> fields, int errorCode, JSONObject response) {
        Context context = App.getContext();

        HashMap<String, Integer> errorMessages = new HashMap<String, Integer>();
        errorMessages.put("required", R.string.error_field_required);
        errorMessages.put("unique", R.string.error_field_duplicate);

        if (errorCode == 400) {
            try {
                JSONObject errors = response.getJSONObject("errors");

                Iterator iterator = errors.keys();
                while(iterator.hasNext()){
                    String fieldName = (String)iterator.next();
                    JSONObject fieldError = errors.getJSONObject(fieldName);

                    TextView field = fields.get(fieldName);
                    if (field != null) {
                        String errorType = fieldError.getString("type");

                        if (errorType.equals("user defined")) {
                            errorType = fieldError.getString("message");
                        }

                        Integer errorMessageCode = errorMessages.get(errorType);

                        if (errorMessageCode != null) {
                            String errorMessage = context.getString(errorMessageCode);
                            field.setError(errorMessage);
                        } else {
                            ToastTools.use().longToast(R.string.internal_error);
                            Log.e(tag, "Error " + errorType + " is not handled by the application (on field " + fieldName + ").");
                        }


                    } else {
                        ToastTools.use().longToast(R.string.internal_error);
                        Log.e(tag, "Field " + fieldName + " is not handled by the application.");
                    }
                }
            } catch (Exception e) {
                ToastTools.use().longToast(R.string.unknown_error);
                Log.e(tag, e.getMessage() + ". Response was: " + response.toString());
            }
        } else {
            ToastTools.use().longToast(R.string.internal_error);
            Log.e(tag, "Error code " + errorCode + " is not handled by the application.");
        }
    }
}
