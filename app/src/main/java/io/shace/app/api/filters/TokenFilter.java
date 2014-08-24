package io.shace.app.api.filters;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by melvin on 8/23/14.
 *
 * Due to a bug in Android, you MUST set the inputType to "textNoSuggestions"
 */
public class TokenFilter implements InputFilter {
    private static final String TAG = TokenFilter.class.getSimpleName();

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned destination, int dStart, int dEnd) {
        if (source.length() > 0) {
            return source.toString().replaceAll("[^a-zA-Z0-9|-]*", "");
        }
        return null;
    }
}
