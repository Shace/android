package io.shace.app.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.androidannotations.annotations.EActivity;

import io.shace.app.BaseActivity;
import io.shace.app.R;

@EActivity
public class EventActivity extends BaseActivity {
    private static final String TAG = EventActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        if (savedInstanceState == null) {
            Log.i(TAG, "IN");
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new EventFragment_())
                    .commit();
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        return customUpNavigation();
    }
}
