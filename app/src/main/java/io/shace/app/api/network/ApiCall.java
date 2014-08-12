package io.shace.app.api.network;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import io.shace.app.tools.NetworkTools;


/**
 * Created by melvin on 4/29/14.
 *
 * Abstract class to handle the API calls
 */

public abstract class ApiCall {
    private static final String TAG = ApiCall.class.getSimpleName();

    public ApiCall() {
    }

    /**
     * Send a GET request to the specified URL using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param data map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     */
    abstract void get(String uri, Map<String, String> data);

    /**
     * Send a GET request to the specified URL using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param data map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public abstract void get(String uri, Map<String, String> data, ApiResponseCallbacks response);

    /**
     * Send a POST request to the specified URL using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param data map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     */
    abstract void post(String uri, Map<String, String> data);

    /**
     * Send a POST request to the specified URL using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param data map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    abstract void post(String uri, Map<String, String> data, ApiResponseCallbacks response);

    /**
     * Send a PUT request to the specified URL using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param data map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     */
    abstract void put(String uri, Map<String, String> data);

    /**
     * Send a PUT request to the specified URL using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param data map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    abstract void put(String uri, Map<String, String> data, ApiResponseCallbacks response);

    /**
     * Callback for Volley, called when the request fails (whether the server sent a 404 error code,
     * or the server is unreachable)
     *
     * @param userResponse Instance of ApiResponse to call the user's callbacks
     * @return Response.ErrorListener()
     */
    protected Response.ErrorListener _error(final ApiResponseCallbacks userResponse) {
        /**
         * Check the kind of error and call the callbacks accordingly
         */
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                boolean found = false;
                int[] codes = userResponse.getAllowedCodes();
                int currentErrorCode = 0;

                if (error.networkResponse != null) { // Server not down
                    currentErrorCode = error.networkResponse.statusCode;

                    for (int code : codes) {
                        if (code == currentErrorCode) {
                            found = true;
                            break;
                        }
                    }
                }

                if (found) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(responseBody);
                            userResponse.onError(currentErrorCode, jsonResponse);
                        } catch (JSONException e) {
                            userResponse.onError(currentErrorCode, responseBody);
                        }
                    } catch (UnsupportedEncodingException e) {
                        NetworkTools.sendServerError();
                        Log.e(TAG, e.getMessage());
                    }
                } else if (error.networkResponse == null) {
                    NetworkTools.sendServerError();
                    Log.e(TAG, "Volley networkResponse is null. Error was: " + error.toString());
                } else {
                    NetworkTools.sendServerError();
                    Log.e(TAG, error.toString());
                }
                userResponse.alwaysAfter();
            }
        };
    }

    /**
     * Callback for Volley, called when the request succeed (whether the server sent a 404 error code,
     * or the server is unreachable)
     *
     * @param userResponse Instance of ApiResponse to call the user's callbacks
     * @return Response.Listener()
     */
    protected Response.Listener<JSONObject> _success(final ApiResponseCallbacks userResponse) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                userResponse.onSuccess(response);
                userResponse.alwaysAfter();
            }
        };
    }
}
