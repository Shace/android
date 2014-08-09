package io.shace.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

@WindowFeature({
        Window.FEATURE_NO_TITLE,
        Window.FEATURE_INDETERMINATE_PROGRESS
})
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(R.id.signInText)
    protected void signIn() {
//        Intent intent = new Intent(this, SignInFragment.class);
//        startActivity(intent);
    }

    @Click(R.id.signUpButton)
    protected void signUp() {
//        Intent intent = new Intent(this, SignInFragment.class);
//        startActivity(intent);
    }
}
