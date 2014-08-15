package io.shace.app.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.App;
import io.shace.app.R;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.User;
import io.shace.app.api.network.ApiResponseCallbacks;
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
        exec();
    }

    /**
     * Execute the query with the given model
     *
     * @param model Model to use for the query
     */
    public void exec(Model model) {
        mData = model.mapData();
        exec();
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
     * Transform a JSONObject into a list of object
     * Example: {@code List<User> users = jsonObjectToList(json)}
     *
     * TODO: Fix hard code of Event
     *
     * @param json
     *
     * @return list of T
     */
    protected List<Event> jsonArrayToList(JSONArray json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Event>>() {}.getType();

        return gson.fromJson(json.toString(), listType);
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

            if (error.is(ApiError.TOKEN_NOT_FOUND)) {
               User.signOut();
            }
        } catch (JSONException e) {
            error = null;
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
