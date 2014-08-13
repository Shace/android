package io.shace.app.tools;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import io.shace.app.App;

/**
 * Created by melvin on 8/12/14.
 */
public class IntentTools {
    private static final String TAG = IntentTools.class.getSimpleName();
    private static final String ERROR = "Activity is null, try to give 'this' as first parameter, or call the method after onResume()";

    /**
     * Start a new intent using the current activity
     *
     * @param cls class to use
     */
    public static void newBasicIntent(Class<?> cls) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ERROR);
        }

        newBasicIntent(activity, cls);
    }

    /**
     * Start a new intent using the specified activity
     *
     * @param activity activity to use (should probably be this)
     * @param cls class to use
     */
    public static void newBasicIntent(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    /**
     * Start a new intent as there was no other before
     *
     * @param cls class to use
     */
    public static void newFullIntent(Class<?> cls) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ERROR);
        }

        newFullIntent(activity, cls);
    }


    /**
     * Start a new intent as there was no other before
     *
     * @param activity activity to use (should probably be this)
     * @param cls class to use
     */
    public static void newFullIntent(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.overridePendingTransition(0, 0);
        activity.finishAffinity();

    }
}
