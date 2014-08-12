package io.shace.app.api.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.Routes;
import io.shace.app.api.network.utilities.EmptyApiResponse;
import io.shace.app.api.models.Token;
import io.shace.app.tools.NetworkTools;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 4/29/14.
 */

public class ApiCall {
    private static final String TAG = ApiCall.class.getSimpleName();

    public ApiCall() {}

    /*
     * POST
     */

    /**
     * Send a POST request to the specified URL using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param data map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void post(String uri, Map<String, String> data, ApiResponseCallbacks response) {
        _post(uri, data, response);
    }

    /**
     * Send a POST request to the specified URL using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param data map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     */
    public void post(String uri, Map<String, String> data) {
        _post(uri, data, null);
    }


    // TODO Allow GET data too (ex. to POST on /event/:eventId/)
    private void _post(String url, Map<String, String> data, ApiResponseCallbacks response) {
        // We removed the optional variables that have not been given
        url = url.replaceAll(Routes.VARIABLES_REGEX, "");

        makeRequest(Request.Method.POST, url, new JSONObject(data), response);
    }

    /*
     * PUT
     */

    /**
     * Send a PUT request to the specified URL using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param data map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void put(String uri, Map<String, String> data, ApiResponseCallbacks response) {
        _put(uri, data, response);
    }

    /**
     * Send a PUT request to the specified URL using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param data map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     */
    public void put(String uri, Map<String, String> data) {
        _put(uri, data, null);
    }


    private void _put(String url, Map<String, String> data, ApiResponseCallbacks response) {
        if (data != null) {
            Iterator<Map.Entry<String,String>> iterator = data.entrySet().iterator();

            while (iterator.hasNext()) {
                String oldUrl = url;
                Map.Entry<String,String> entry = iterator.next();
                url = url.replaceAll("::?" + entry.getKey(), entry.getValue());

                if(url.equals(oldUrl) == false){
                    iterator.remove();
                }
            }
        }

        // We removed the optional variables that have not been given
        url = url.replaceAll(Routes.VARIABLES_REGEX, "");

        makeRequest(Request.Method.PUT, url, new JSONObject(data), response);
    }

    /*
     * GET
     */

    /**
     * Send a GET request to the specified URL using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param data map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     */
    public void get(String uri, Map<String, String> data) {
        _get(uri, data, null);
    }

    /**
     * Send a GET request to the specified URL using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param data map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void get(String uri, Map<String, String> data, ApiResponseCallbacks response) {
        _get(uri, data, response);
    }


    private void _get(String url, Map<String, String> data, ApiResponseCallbacks response) {
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                url = url.replaceAll("::?" + entry.getKey(), entry.getValue());
            }
        }

        // We removed the optional variables that have not been given
        url = url.replaceAll(Routes.VARIABLES_REGEX, "");

        makeRequest(Request.Method.GET, url, null, response);
    }

    /*
     * MakeRequest
     */

    /**
     * Make and log a request then call the callbacks accordingly.
     * A Toast will be display in case the device has no internet connection
     *
     * @param method method to call (GET, POST)
     * @param url full url to call
     * @param data POST data
     * @param response instance of ApiResponse to handle the callbacks
     */
    protected void makeRequest(final int method, String url, final JSONObject data, final ApiResponseCallbacks response) {
        if (NetworkTools.hasInternet()) {
            Token token = Token.get();

            if (token != null) {
                url = url.replaceAll(":access_token", token.getToken());
            }

            _makeRequest(method, url, data, response);
        } else {
            ToastTools.use().longToast(R.string.no_internet);
        }
    }

    private void _makeRequest(int method, String url, JSONObject data, ApiResponseCallbacks response) {
        response = (response == null) ? (new EmptyApiResponse()) : (response);
        String methodName = (method == Request.Method.GET) ? "GET" : "POST";
        Log.i(TAG, methodName + " " + url);
        JsonObjectRequest req = new JsonObjectRequest(method, url, data, _success(response), _error(response));
        ApiRequestQueue.getInstance().add(req, TAG);
        response.alwaysBefore();
    }


    /*
     * Callbacks
     */


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
}