package io.shace.app.tools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.shace.app.App;

/**
 * Created by melvin on 8/11/14.
 */
public class PreferenceTools {
    public static final String KEY_BETA_STATUS = "beta.status";
    public static final String KEY_FIRST_LAUNCH = "app.first_launch";
    public static final String KEY_TOKEN = "token";

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    public static SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    /*
     * getKey overload
     */

    public static int getKey(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    public static String getKey(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    public static boolean getKey(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    public static long getKey(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    public static float getKey(String key, float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    /*
     * putKey overload
     */

    public static void putKey(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static void putKey(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static void putKey(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static void putKey(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public static void putKey(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public static void removeKey(String key) {
        getEditor().remove(key).apply();
    }
}
