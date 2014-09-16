package io.shace.app.api.network;

import android.util.Log;
import android.util.SparseArray;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.Routes;
import io.shace.app.api.models.Token;
import io.shace.app.api.network.requests.ApiJsonObjectRequest;
import io.shace.app.api.network.utilities.EmptyApiResponse;
import io.shace.app.tools.NetworkTools;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 4/29/14
 */

public class ApiCall {
    private static final String TAG = ApiCall.class.getSimpleName();
    private final SparseArray<String> mMethodToString = initMethodToString();

    private SparseArray<String> initMethodToString() {
        SparseArray<String> array = new SparseArray<String>();
        array.put(Request.Method.GET, "GET");
        array.put(Request.Method.HEAD, "HEAD");
        array.put(Request.Method.DELETE, "DELETE");
        array.put(Request.Method.OPTIONS, "OPTIONS");
        array.put(Request.Method.PATCH, "PATCH");
        array.put(Request.Method.PUT, "PUT");
        array.put(Request.Method.POST, "POST");
        array.put(Request.Method.TRACE, "TRACE");

        return array;
    }

    private Object mRequestTag = null;

    public ApiCall(Object tag) {
        mRequestTag = tag;
    }

    /*
     * POST
     */

    /**
     * Send a POST request to the specified URI using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param uriData map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void post(String uri, Map<String, String> uriData, ApiResponseCallbacks response) {
        makeRequest(Request.Method.POST, uri, uriData, null, response);
    }

    /**
     * Send a POST request to the specified URI using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param uriData JsonObject to use
     * @param bodyData JsonObject to use
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void post(String uri, Map<String, String> uriData, JsonObject bodyData, ApiResponseCallbacks response) {
        makeRequest(Request.Method.POST, uri, uriData, bodyData, response);
    }

    /*
     * PUT
     */

    /**
     * Send a PUT request to the specified URI using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param uriData map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void put(String uri, Map<String, String> uriData, ApiResponseCallbacks response) {
        makeRequest(Request.Method.PUT, uri, uriData, null, response);
    }

    /**
     * Send a PUT request to the specified URI using the data
     *
     * @param uri the uri (without the protocol nor the domain) to GET
     * @param uriData map containing the data to inject in the uri. The method will look for
     *             the data keys in the uri param and replace all occurrences by the its associated value
     * @param bodyData map containing the data to inject in the body.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void put(String uri, Map<String, String> uriData, JsonObject bodyData, ApiResponseCallbacks response) {
        makeRequest(Request.Method.PUT, uri, uriData, bodyData, response);
    }

    /*
     * GET
     */

    /**
     * Send a GET request to the specified URI using the data, and call the given callbacks.
     *
     * @param uri the uri (without the protocol nor the domain) to POST
     * @param uriData map containing the data to post.
     * @param response instance of ApiResponse to handle the callbacks
     */
    public void get(String uri, Map<String, String> uriData, ApiResponseCallbacks response) {
       makeRequest(Request.Method.GET, uri, uriData, null, response);
    }

    /*
     * MakeRequest
     */

    /**
     * Make and log a request then call the callbacks accordingly.
     * A Toast will be display in case the device has no internet connection
     *
     * @param method method to call (GET, POST)
     * @param uri full uri to call
     * @param uriData POST data
     * @param bodyData POST data
     * @param response instance of ApiResponse to handle the callbacks
     */
    protected void makeRequest(final int method, String uri, Map<String, String> uriData, final JsonObject bodyData, final ApiResponseCallbacks response) {
        if (NetworkTools.hasInternet()) {

            if (uriData != null) {
                Iterator<Map.Entry<String,String>> iterator = uriData.entrySet().iterator();

                while (iterator.hasNext()) {
                    String oldUri = uri;
                    Map.Entry<String,String> entry = iterator.next();
                    uri = uri.replaceAll("::?" + entry.getKey(), entry.getValue());

                    if(uri.equals(oldUri) == false){
                        iterator.remove();
                    }
                }
            }

            JSONObject body = null;
            if (method == Request.Method.POST || method == Request.Method.PUT) {
                // We removed the optional variables that have not been given
                uri = uri.replaceAll(Routes.VARIABLES_REGEX, "");

                try {
                    body = (bodyData == null) ? (new JSONObject(uriData)) : (new JSONObject(bodyData.toString()));
                } catch (JSONException e) {
                    Log.e(TAG, "invalid json: " + bodyData.toString());
                }
            }


            Token token = Token.get();

            if (token != null) {
                uri = uri.replaceAll(":access_token", token.getToken());
            }

            _makeRequest(method, uri, body, response);
        } else {
            ToastTools.use().longToast(R.string.no_internet);
        }
    }

    private void _makeRequest(int method, String uri, JSONObject body, ApiResponseCallbacks response) {
        response = (response == null) ? (new EmptyApiResponse()) : (response);
        String methodName = mMethodToString.get(method, "Unknown");

        Log.i(TAG, methodName + " " + uri);
        Log.d(TAG, "Body: " + ((body == null) ? ("None") : (body.toString())));

        ApiJsonObjectRequest req = new ApiJsonObjectRequest(method, uri, body, _success(response), _error(response));
        Object tag = (mRequestTag != null) ? mRequestTag : TAG;
        RequestQueue.getInstance().add(req, tag);
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
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        NetworkTools.sendTimeOutError();
                    } else {
                        NetworkTools.sendServerError();
                        Log.e(TAG, "Volley networkResponse is null. Error was: " + error.toString());
                    }

                } else {
                    NetworkTools.sendServerError();
                    Log.e(TAG, error.toString());
                }
                userResponse.alwaysAfter();
            }
        };
    }
}