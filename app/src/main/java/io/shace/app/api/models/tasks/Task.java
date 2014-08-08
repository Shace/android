package io.shace.app.api.models.tasks;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import io.shace.app.App;
import io.shace.app.api.ApiResponseCallbacks;
import io.shace.app.api.models.Model;
import io.shace.app.api.models.listeners.TokenListener;

/**
 * Created by melvin on 8/7/14.
 */
abstract public class Task implements ApiResponseCallbacks {
    private static final String TAG = Task.class.getSimpleName();
    protected static Context sContext = App.getContext();

    protected TokenListener mListener;

    /*
    * HTTP codes allowed in the error callback
    */
    private int[] mAllowedCodes = {};

    public int[] getAllowedCodes() {
        return mAllowedCodes;
    }
    public void setAllowedCodes(int[] allowedCodes) {
        this.mAllowedCodes = mAllowedCodes;
    }

    /**
     * Execute the query without any data
     */
    public void exec() {
        HashMap<String, String> data = new HashMap<String, String>();
        exec(data);
    }

    /**
     * Execute the query with the given data
     *
     * @param data Data to use for the query
     */
    public abstract void exec(HashMap<String, String> data);


    /**
     * Transform a JSONObject into a model
     * Example: {@code User user = jsonObjectToModel(json, User.class)}
     *
     * @param json
     * @param type Type of the class you want
     * @param <T> class that extends model
     *
     * @return instance of T
     */
    protected <T extends Model> T jsonObjectToModel(JSONObject json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json.toString(), type);
    }


    @Override
    public void alwaysBefore() {
        mListener.onPreExecute();
    }

    @Override
    public void alwaysAfter() {
        mListener.onPostExecute();
    }

}
