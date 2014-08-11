package io.shace.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by melvin on 8/10/14.
 */
public class BaseActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
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
}
