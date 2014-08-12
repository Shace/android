package io.shace.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;

import io.shace.app.api.ApiError;

/**
 * Created by melvin on 8/10/14.
 */
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        App.setCurrentActivity(this);
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = App.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            App.setCurrentActivity(null);
    }

    protected boolean checkFormError(ApiError error, Map<String, TextView> fields) {
        Map<String,String> params = error.getParameters();

        if (params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String field = entry.getKey();
                String type = entry.getValue();

                TextView view = fields.get(field);
                if (view != null) {
                    String message = ApiError.getErrorMessage(type);

                    if (message != null) {
                        view.setError(message);
                    }
                    else {
                        view.setError("Unknown error");
                    }
                } else {
                    Log.e(TAG, "FormValidation error: Unknown field '" + field + "'");
                }
            }
            return true;
        }
        return false;
    }
}
