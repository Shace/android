package io.shace.app.tools;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.Map;

import io.shace.app.App;

/**
 * Created by melvin on 8/12/14.
 */
public class IntentTools {
    private static final String TAG = IntentTools.class.getSimpleName();
    private static final String ACTIVITY_ERROR = "Activity is null, try to give 'this' as first parameter, or call the method after onResume()";

    /**
     * Start a new intent using the current activity
     *
     * @param cls class to use
     */
    public static void newBasicIntent(Class<?> cls) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ACTIVITY_ERROR);
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
        intent.putExtra("caller", activity.getClass().getName());
        activity.startActivity(intent);
    }

    /**
     * Start a new intent using the specified activity
     *
     * @param activity activity to use (should probably be this)
     * @param cls class to use
     * @param extra key of the extra value
     * @param value value of the extra
     */
    public static void newBasicIntentWithExtraString(Activity activity, Class<?> cls, String extra, String value) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra("caller", activity.getClass().getName());
        intent.putExtra(extra, value);
        activity.startActivity(intent);
    }

    /**
     * Start a new intent using the current activity
     *
     * @param cls class to use
     * @param extra key of the extra value
     * @param value value of the extra
     */
    public static void newBasicIntentWithExtraString(Class<?> cls, String extra, String value) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ACTIVITY_ERROR);
        }

        newBasicIntentWithExtraString(activity, cls, extra, value);
    }

    /**
     * Start a new intent using the specified activity
     *
     * @param activity activity to use (should probably be this)
     * @param cls class to use
     * @param extras Map of extra strings
     */
    public static void newBasicIntentWithExtraStrings(Activity activity, Class<?> cls, Map<String, String> extras) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra("caller", activity.getClass().getName());
        for (Map.Entry<String, String> entry : extras.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        activity.startActivity(intent);
    }

    /**
     * Start a new intent using the current activity
     *
     * @param cls class to use
     * @param extras Map of extra strings
     */
    public static void newBasicIntentWithExtraStrings(Class<?> cls, Map<String, String> extras) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ACTIVITY_ERROR);
        }

        newBasicIntentWithExtraStrings(activity, cls, extras);
    }

    /**
     * Start a new intent as there was no other before
     *
     * @param cls class to use
     */
    public static void newFullIntent(Class<?> cls) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ACTIVITY_ERROR);
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

    /**
     * Replace the current activity by a new one
     *
     * @param cls class to use
     */
    public static void newReplacingIntent(Class<?> cls) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ACTIVITY_ERROR);
        }

        newReplacingIntent(activity, cls);
    }

    /**
     * Replace the current activity by a new one
     *
     * @param activity activity to use (should probably be this)
     * @param cls class to use
     */
    public static void newReplacingIntent(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }



    /**
     * Replace the current activity by a new one, with an extra string
     *
     * @param cls class to use
     */
    public static void newReplacingIntentWithExtraString(Class<?> cls, String extra, String value) {
        Activity activity = App.getCurrentActivity();

        if (activity == null) {
            Log.e(TAG, ACTIVITY_ERROR);
        }

        newReplacingIntentWithExtraString(activity, cls, extra, value);
    }

    /**
     * Replace the current activity by a new one, with an extra string
     *
     * @param activity activity to use (should probably be this)
     * @param cls class to use
     */
    public static void newReplacingIntentWithExtraString(Activity activity, Class<?> cls, String extra, String value) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(extra, value);
        activity.startActivity(intent);
        activity.finish();
    }
}
