package io.shace.app.tools;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;

import io.shace.app.App;

/**
 * Created by melvin on 9/5/14.
 *
 * This class is used to deal with dimensions
 */
public class MetricTools {
    /**
     * Convert DP to Pixel
     *
     * @param dp
     * @return the number of pixel
     */
    public static float dpToPx(int dp) {
        Resources r = App.getContext().getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    /**
     * Convert DP to Pixel
     *
     * @param dp
     * @return the number of pixel
     */
    public static float dpToPx(float dp) {
        Resources r = App.getContext().getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    /**
     * Convert pixel to DP
     *
     * @param pixel
     * @return the number of dp
     */
    public static float pxToDp(int pixel) {
        float scale = App.getContext().getResources().getDisplayMetrics().density;
        return pixel / scale + 0.5f;
    }


    /**
     * Convert pixel to DP
     *
     * @param pixel
     * @return the number of dp
     */
    public static float pxToDp(float pixel) {
        float scale = App.getContext().getResources().getDisplayMetrics().density;
        return pixel / scale + 0.5f;
    }

    /**
     * Return the size of the current Action bat
     * @param activity
     * @return the size of the action bar or 0
     */
    public static int getActionbarSize(Activity activity) {
        TypedValue tv = new TypedValue();

        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }

        return 0;
    }
}
