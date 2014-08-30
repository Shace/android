package io.shace.app.ui.boot;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import io.shace.app.App;
import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.TokenListener;
import io.shace.app.api.models.Token;
import io.shace.app.api.models.User;
import io.shace.app.tools.IntentTools;


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
        Class<?> cls = null;

        if (User.isLogged()) {
            //cls = MainActivity_.class;
        } else {
            if (App.isFirstLaunch()) {
                // cls = TourActivity.class;
                cls = WelcomeActivity_.class;
            } else {
                cls = WelcomeActivity_.class;
            }
        }

        IntentTools.newFullIntent(this, cls);
    }

    @Override
    public void onTokenCreated(Token token) {
        startApp();
    }

    @Override
    public void onTokenCreatedFail(ApiError error) {
        // Todo retry x times then display the appropriate errors + play with onResume/onPause to reload
    }

    @Override
    public void onTokenUpdated(Token token) {}

    @Override
    public void onTokenUpdatedFail(ApiError error) {}

    @Override
    public void onPreExecute() {}

    @Override
    public void onPostExecute() {}
}
