package io.shace.app.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.models.User;
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
     * POST
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
     * PUT
     */

    @Override
    public void put(String uri, HashMap<String, String> data, ApiResponse response) {
        _put(uri, data, response);
    }

    @Override
    public void put(String uri, HashMap<String, String> data) {
        _post(uri, data, null);
    }


    private void _put(String url, HashMap<String, String> data, ApiResponse response) {
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
    protected void makeRequest(final int method, String url, final JSONObject data, final ApiResponse response) {
        if (NetworkTools.hasInternet(mContext)) {
            String token = User.getAccessToken(mContext);

            if (token != null) {
                url = url.replaceAll(":access_token", token);
            }

//            TODO: API 2.0. Remove the _makerequest and uncomment the following
                    _makeRequest(method, url, data, response);
//            if (User.sessionHasExpired(mContext)) {
//                User.refreshToken(mContext, new StringCallback(){
//                    @Override
//                    public void onSuccess(String newToken) {
//                        // TODO: Replace the access_token=[A-Za-z0-9-] by newToken
//                        _makeRequest(method, url, data, response);
//                    }
//                });
//            } else {
//                _makeRequest(method, url, data, response);
//            }
        } else {
            ToastTools.use().longToast(mContext, R.string.no_internet);
        }
    }

    private void _makeRequest(int method, String url, JSONObject data, ApiResponse response) {
        response = (response == null) ? (new ApiResponse()) : (response);
        String methodName = (method == Request.Method.GET) ? "GET" : "POST";
        VolleyLog.v(methodName + " " + url);
        JsonObjectRequest req = new JsonObjectRequest(method, url, data, _success(response), _error(response));
        ApiRequestQueue.getInstance(mContext).add(req, TAG);
    }
}