package io.shace.app.api.models;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.shace.app.callbacks.EmptyCallback;
import io.shace.app.R;
import io.shace.app.SignInFragment_;
import io.shace.app.callbacks.StringCallback;
import io.shace.app.api.ApiResponse;
import io.shace.app.api.AsyncApiCall;
import io.shace.app.api.Routes;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 7/15/14.
 * TODO: Clean the code (some functions are not linked to the users)
 */
public class User {
    private static final String TAG = "User";

    /**
     * Sign out the current user
     *
     * @param activity
     */
    static public void signOut(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences("settings", Context.MODE_APPEND);
        settings.edit().clear().apply();

        // TODO: Put in a tool
        Fragment fragment = new SignInFragment_();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    static public void connectAsGuest(final Context context, final EmptyCallback callback) {
        HashMap<String,String> postData = new HashMap<String, String>();
        postData.put("password", "");
        postData.put("auto_renew", "true");

        new AsyncApiCall(context).post(Routes.ACCESS_TOKEN, postData,
                new ApiResponse(new int[]{}) {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            SharedPreferences.Editor settings = context.getSharedPreferences("settings", Context.MODE_APPEND).edit();
                            settings.putString("accessToken", response.getString("token"));
                            settings.putLong("creation", response.getLong("creation"));
                            settings.putLong("expiration", response.getLong("expiration"));
                            settings.apply();
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            ToastTools.use().longToast(context, R.string.internal_error);
                        }

                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
    }

    static public String getAccessToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return settings.getString("accessToken", null);
    }


    /**
     * refresh the access_token
     * TODO: API 2.0
     *
     * @param context Context of the caller
     * @param callback callback to call or null
     */
    @Deprecated
    static public void refreshToken(final Context context, final StringCallback callback) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("access_token", getAccessToken(context));

        new AsyncApiCall(context).noToken().get(Routes.ACCESS_TOKEN, data,
                new ApiResponse(new int[]{401}) {
                    @Override
                    public void onSuccess(JSONObject response) {
                        String token = "";

                        SharedPreferences.Editor settings = context.getSharedPreferences("settings", Context.MODE_APPEND).edit();
                        try {
                            token = response.getString("token");
                            settings.putString("accessToken", token);
                            settings.apply();
                        } catch (JSONException e) {
                            Log.e(TAG, "token not found");
                        }
                        callback.onSuccess(token);
                    }
                }
        );
    }

    /**
     * Check if the user is properly logged with a user account
     * DOES NOT VALIDATE THE ACCESS_TOKEN
     *
     * @param context Context of the caller
     * @return True if the user is logged, false otherwise
     */
    static public boolean isLogged(Context context) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String token = settings.getString("accessToken", null);
        int userId = settings.getInt("userId", -1);
        long creation = settings.getLong("creation", 0);
        long expiration = settings.getLong("expiration", 0);

        return (token != null && userId > -1 && creation > 0 && expiration > 0);
    }


    /**
     * Check if the user has been authenticated using whether a user or guest account
     * DOES NOT VALIDATE THE ACCESS_TOKEN
     *
     * @param context Context of the caller
     * @return True if the user is authenticated, false otherwise
     */
    static public boolean isAuthenticated(Context context) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String token = settings.getString("accessToken", null);
        int userId = settings.getInt("userId", -1);
        long creation = settings.getLong("creation", 0);
        long expiration = settings.getLong("expiration", 0);

        return (token != null && creation > 0 && expiration > 0);
    }


    /**
     * Check if the access_token is still valid
     * TODO: API 2.0
     *
     * @param context Context of the caller
     * @return True if the token has expired, false otherwise
     */
    @Deprecated
    static public boolean sessionHasExpired(Context context) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String token = settings.getString("accessToken", null);
        int userId = settings.getInt("userId", -1);
        long creation = settings.getLong("creation", 0);
        long expiration = settings.getLong("expiration", 0);

        if (token != null && creation > 0 && expiration > 0) {
            return (expiration < System.currentTimeMillis() / 1000);
        }
        return true;
    }
}
