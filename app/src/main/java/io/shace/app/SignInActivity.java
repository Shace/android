package io.shace.app;

import android.app.Activity;
import android.view.Window;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

@WindowFeature({
        Window.FEATURE_NO_TITLE,
        Window.FEATURE_INDETERMINATE_PROGRESS
})
@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends Activity {

}
