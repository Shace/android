package io.shace.app.api.models.tasks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.shace.app.App;
import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.ApiResponseCallbacks;
import io.shace.app.api.models.Model;
import io.shace.app.api.models.listeners.Listener;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 8/7/14.
 */
abstract public class Task implements ApiResponseCallbacks {
    private static final String TAG = Task.class.getSimpleName();
    protected static Context sContext = App.getContext();

    /**
     * Generic Listener for all the basic actions
     *
     * Todo: Find way not to duplicate the listener in the children
     */
    protected Listener mGenericListener;

    /**
     * Contains the data provided by exec(Model) or exec(Map)
     */
    protected Map<String, String> mData = new HashMap<String, String>();

    /*
    * HTTP codes allowed in the error callback
    */
    private int[] mAllowedCodes = {};

    public int[] getAllowedCodes() {
        return mAllowedCodes;
    }
    public void setAllowedCodes(int[] allowedCodes) {
        mAllowedCodes = allowedCodes;
    }

    public void setGenericListener(Listener mGenericListener) {
        this.mGenericListener = mGenericListener;
    }

    /**
     * Execute the query
     */
    public abstract void exec();

    /**
     * Execute the query using the given data
     *
     * @param data Data to use for the query
     */
    public void exec(Map<String, String> data) {
        mData = data;
    }

    /**
     * Execute the query with the given model
     *
     * @param model Model to use for the query
     */
    public void exec(Model model) {
        mData = model.mapData();
    }


    /**
     * Transform a JSONObject into an object
     * Example: {@code User user = jsonObjectToObject(json, User.class)}
     *
     * @param json
     * @param type Type of the class you want
     *
     * @return instance of T
     */
    protected <T> T jsonObjectToObject(JSONObject json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json.toString(), type);
    }

    /**
     * Create an error object.
     *
     * @param response
     * @return an object representing the error or null
     */
    protected ApiError getError(JSONObject response) {
        ApiError error = null;

        try {
            error = jsonObjectToObject(response.getJSONObject("error"), ApiError.class);
        } catch (JSONException e) {
            error = null;
            Log.e(TAG, "No 'error' key found in " + response.toString());
            ToastTools.use().longToast(R.string.internal_error);
        }
        return error;
    }

    @Override
    public void alwaysBefore() {
        mGenericListener.onPreExecute();
    }

    @Override
    public void alwaysAfter() {
        mGenericListener.onPostExecute();
    }

    @Override
    public void onError(int code, String response) {
        Log.v(TAG, response);
        ToastTools.use().longToast(response);
    }
}
