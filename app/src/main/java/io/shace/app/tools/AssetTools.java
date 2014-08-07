package io.shace.app.tools;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.shace.app.App;

/**
 * Created by melvin on 5/7/14.
 */
public class AssetTools {
    private static final String API_URL_KEY = "dev.api.url";
    private static final String TAG = "AssetTools";
    protected static Context sContext = App.getContext();

    static public Properties getProperties(String filePath) {
        Properties prop = new Properties();

        try {
            InputStream inputStream = sContext.getAssets().open(filePath);
            prop.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to open " + filePath + " file");
            prop = null;
        }
        return prop;
    }

    static public Properties getProjectSettings() {
        String filename = "shace.properties";
        Properties prop = AssetTools.getProperties(filename);

        if (prop == null) {
            throw new RuntimeException(filename + " does not exists");
        }
        return prop;
    }

    static public String getDevApiUrl() {
        Properties prop = getProjectSettings();
        String apiUrl = prop.getProperty(API_URL_KEY, null);
        if (apiUrl == null) {
            throw new RuntimeException(API_URL_KEY + " not defined");
        }
        return apiUrl;
    }
}
