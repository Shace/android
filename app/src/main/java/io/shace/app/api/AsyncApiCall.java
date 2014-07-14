package io.shace.app.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.shace.app.R;
import io.shace.app.tools.NetworkTools;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 4/29/14.
 */

public class AsyncApiCall extends ApiCall {
    private static final String TAG = "AsyncApiCall";

    public AsyncApiCall(Context context) {
        super(context);
    }

    /*
     * Post
     */

    @Override
    public void post(String uri, HashMap<String, String> data, ApiResponse response) {
        _post(uri, data, response);
    }

    @Override
    public void post(String uri, HashMap<String, String> data) {
        _post(uri, data, null);
    }


    // TODO Allow GET data too (ex. to POST on /event/:eventId/)
    private void _post(String url, HashMap<String, String> data, ApiResponse response) {
        // We removed the optional variables that have not been given
        url = url.replaceAll(Routes.VARIABLES_REGEX, "");

        makeRequest(Request.Method.POST, url, new JSONObject(data), response);
    }

    /*
     * GET
     */

    @Override
    public void get(String uri, HashMap<String, String> data, ApiResponse response) {
        _get(uri, data, response);
    }

    @Override
    public void get(String uri, HashMap<String, String> data) {
        _get(uri, data, null);
    }

    private void _get(String url, HashMap<String, String> data, ApiResponse response) {
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                url = url.replaceAll("::?" + entry.getKey(), entry.getValue());
            }
        }

        // We removed the optional variables that have not been given
        url = url.replaceAll(Routes.VARIABLES_REGEX, "");

        makeRequest(Request.Method.GET, url, null, response);
    }

    /**
     * Make and log a request then call the callbacks accordingly.
     * A Toast will be display in case the device has no internet connection
     *
     * @param method method to call (GET, POST)
     * @param url full url to call
     * @param data POST data
     * @param response instance of ApiResponse to handle the callbacks
     */
    protected void makeRequest(int method, String url, JSONObject data, ApiResponse response) {
        response = (response == null) ? (new ApiResponse()) : (response);

        if (NetworkTools.hasInternet(mContext)) {
            String methodName = (method == Request.Method.GET) ? "GET" : "POST";
            VolleyLog.v(methodName + " " + url);
            JsonObjectRequest req = new JsonObjectRequest(method, url, data, _success(response), _error(response));
            ApiRequestQueue.getInstance(mContext).add(req, TAG);
        } else {
            ToastTools.use().longToast(mContext, R.string.no_internet);
        }
    }
}