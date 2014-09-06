package io.shace.app.tools;

import android.content.res.Resources;
import android.util.TypedValue;

import io.shace.app.App;

/**
 * Created by melvin on 9/5/14.
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
}
