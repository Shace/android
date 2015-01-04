package io.shace.app.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import io.shace.app.BaseActivity;
import io.shace.app.R;

@EActivity
public class MediaActivity extends BaseActivity implements ImageFragment.OnFragmentInteractionListener {
    private static final String TAG = MediaActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MediaFragment_()).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        return customUpNavigation();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
