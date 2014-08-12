package io.shace.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(R.id.signInText)
    protected void signIn() {
        Intent intent = new Intent(this, SignInActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.signUpButton)
    protected void signUp() {
        Intent intent = new Intent(this, SignUpActivity_.class);
        startActivity(intent);
    }
}
