package io.shace.app.tools;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by melvin on 5/7/14.
 */
public class AssetTools {
    public static final String API_URL_KEY = "dev.api.url";

    static private final String TAG = "AssetTools";

    static public Properties getProperties(Context context, String filePath) {
        Properties prop = new Properties();

        try {
            InputStream inputStream = context.getAssets().open(filePath);
            prop.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to open " + filePath + " file");
            prop = null;
        }
        return prop;
    }

    static public Properties getProjectSettings(Context context) {
        String filename = "shace.properties";
        Properties prop = AssetTools.getProperties(context, filename);

        if (prop == null) {
            throw new RuntimeException(filename + " does not exists");
        }
        return prop;
    }

    static public String getDevApiUrl(Context context) {
        Properties prop = getProjectSettings(context);
        String apiUrl = prop.getProperty(API_URL_KEY, null);
        if (apiUrl == null) {
            throw new RuntimeException(API_URL_KEY + " not defined");
        }
        return apiUrl;
    }
}
