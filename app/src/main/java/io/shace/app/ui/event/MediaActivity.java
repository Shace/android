package io.shace.app.ui.event;

import android.content.Intent;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import io.shace.app.BaseActivity;
import io.shace.app.R;

@EActivity
public class MediaActivity extends BaseActivity {
    private static final String TAG = MediaActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MediaFragment_())
                    .commit();
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        return customUpNavigation();
    }
}
