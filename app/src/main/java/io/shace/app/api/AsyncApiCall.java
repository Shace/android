package io.shace.app.api;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.models.Token;
import io.shace.app.tools.NetworkTools;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 4/29/14.
 */

public class AsyncApiCall extends ApiCall {
    private static final String TAG = "AsyncApiCall";

    public AsyncApiCall() {
    }

    /*
     * POST
     */

    @Override
    public void post(String uri, Map<String, String> data, ApiResponseCallbacks response) {
        _post(uri, data, response);
    }

    @Override
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

    @Override
    public void put(String uri, Map<String, String> data, ApiResponseCallbacks response) {
        _put(uri, data, response);
    }

    @Override
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

    @Override
    public void get(String uri, Map<String, String> data, ApiResponseCallbacks response) {
        _get(uri, data, response);
    }

    @Override
    public void get(String uri, Map<String, String> data) {
        _get(uri, data, null);
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
        VolleyLog.v(methodName + " " + url);
        JsonObjectRequest req = new JsonObjectRequest(method, url, data, _success(response), _error(response));
        ApiRequestQueue.getInstance().add(req, TAG);
        response.alwaysBefore();
    }
}