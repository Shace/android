package io.shace.app;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_home_page)
public class Homepage extends Fragment {
    private static final String TAG = "Homepage";

    public Homepage() {
    }

    @Click
    void signInButton() {
        Log.d(TAG, "Sign In");
    }

    @Click
    void signUpButton() {
        Log.d(TAG, "Sign up");
    }
}