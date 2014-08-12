package io.shace.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by melvin on 8/6/14.
 */
public class App extends Application {
    private static Context mContext;
    private static Activity sCurrentActivity = null;

    public static boolean isFirstLaunch() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean firstLaunch = pref.getBoolean("app.first_launch", true);

        if (firstLaunch) {
            pref.edit().putBoolean("app.first_launch", false).apply();
        }

        return firstLaunch;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }


    public static Activity getCurrentActivity() {
        return sCurrentActivity;
    }
    public static void setCurrentActivity(Activity currentActivity) {
        sCurrentActivity = currentActivity;
    }
}
