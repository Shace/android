package io.shace.app.ui.boot;

import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.tools.IntentTools;
import io.shace.app.ui.user.SignInActivity_;
import io.shace.app.ui.user.SignUpActivity_;


@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(R.id.signInText)
    protected void signIn() {
        IntentTools.newBasicIntent(SignInActivity_.class);
    }

    @Click(R.id.signUpButton)
    protected void signUp() {
        IntentTools.newBasicIntent(SignUpActivity_.class);
    }
}
