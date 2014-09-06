package io.shace.app.ui.event;

import android.content.Intent;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import io.shace.app.R;
import io.shace.app.RefreshActivity;

@EActivity
public class EventActivity extends RefreshActivity {
    private static final String TAG = EventActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        disableGesture();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new EventFragment_())
                    .commit();
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        return customUpNavigation();
    }

    @Override
    public void onRefresh() {
        EventFragment fragment = (EventFragment) getFragmentManager().findFragmentById(R.id.container);
        fragment.refresh();
    }
}
