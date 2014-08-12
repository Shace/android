package io.shace.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import io.shace.app.tools.PreferenceTools;

/**
 * Created by melvin on 8/6/14.
 */
public class App extends Application {
    private static Context mContext;
    private static Activity sCurrentActivity = null;

    public static boolean isFirstLaunch() {
        boolean firstLaunch = PreferenceTools.getKey(PreferenceTools.KEY_FIRST_LAUNCH, true);

        if (firstLaunch) {
            PreferenceTools.putKey(PreferenceTools.KEY_FIRST_LAUNCH, false);
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
