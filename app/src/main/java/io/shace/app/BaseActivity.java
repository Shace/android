package io.shace.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;

import io.shace.app.api.ApiError;
import io.shace.app.api.network.RequestQueue;

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
        RequestQueue.getInstance().cancelPendingRequests();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        RequestQueue.getInstance().cancelPendingRequests();
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

    protected Intent customUpNavigation() {
        String activityName = getIntent().getStringExtra("caller");

        if (activityName != null) {
            try {
                Class cls = Class.forName(activityName);
                Intent intent = new Intent();
                intent.setClass(this, cls);
                return intent;
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Class '" + activityName + "' not found");
            }
        }

        return super.getParentActivityIntent();
    }
}
