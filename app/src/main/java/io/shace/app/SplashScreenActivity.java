package io.shace.app;

import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import io.shace.app.api.models.Token;
import io.shace.app.api.models.User;
import io.shace.app.api.listeners.TokenListener;


@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends BaseActivity implements TokenListener {
    private static String TAG = SplashScreenActivity.class.getSimpleName();

    @AfterViews
    protected void init() {
        if (User.isAuthenticated()) {
            startApp();
        } else {
            Token.generate(this);
        }
    }

    private void startApp() {
        Intent intent = null;

        if (User.isLogged()) {
            intent = new Intent(this, MainActivity_.class);
        } else {
            if (App.isFirstLaunch()) {
                //intent = new Intent(this, TourActivity.class);
                intent = new Intent(this, WelcomeActivity_.class);
            } else {
                intent = new Intent(this, WelcomeActivity_.class);
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onTokenCreated(Token token) {
        startApp();
    }

    @Override
    public void onTokenCreatedFail() {

    }

    @Override
    public void onTokenUpdated(Token token) {}

    @Override
    public void onTokenUpdatedFail() {}

    @Override
    public void onPreExecute() {}

    @Override
    public void onPostExecute() {}
}
