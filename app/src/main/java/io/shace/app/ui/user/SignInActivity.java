package io.shace.app.ui.user;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import io.shace.app.BaseActivity;
import io.shace.app.BuildConfig;
import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.TokenListener;
import io.shace.app.api.models.Token;
import io.shace.app.tools.IntentTools;
import io.shace.app.tools.ToastTools;
import io.shace.app.ui.MainActivity_;


@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity implements TextView.OnEditorActionListener, TokenListener {
    private static final String TAG = SignInActivity.class.getSimpleName();

    @ViewById(R.id.icon_loader) protected ProgressBar mIconLoader;
    @ViewById(R.id.sign_in_form) View mFormView;

    @ViewById(R.id.email) protected AutoCompleteTextView mEmailView;
    @ViewById(R.id.password) protected EditText mPasswordView;

    @AfterViews
    protected void init() {
        if (BuildConfig.DEBUG) {
            mEmailView.setText("admin@shace.io");
            mPasswordView.setText("admin42");
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
        if (actionID == EditorInfo.IME_ACTION_DONE) {
            signIn();
        }
        return true;
    }

    @Click(R.id.signInButton)
    protected void signIn() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        Map<String, String> postData = new HashMap<String, String>();
        postData.put("email", email);
        postData.put("password", password);

        Token.update(this, postData);
    }

    @Override
    public void onPreExecute() {
        mFormView.setVisibility(View.GONE);
        mIconLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute() {
        mIconLoader.setVisibility(View.GONE);
        mFormView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTokenUpdated(Token token) {
        IntentTools.newFullIntent(MainActivity_.class);
    }

    @Override
    public void onTokenUpdatedFail(ApiError error) {
        if (error.is(ApiError.INVALID_IDS)) {
            ToastTools.use().longToast(R.string.error_credentials);
        } else if (error.is(ApiError.BETA_PROCESSING)) {
            ToastTools.use().longToast(R.string.beta_processing);
        } else {
            Log.e(TAG, "Unknown error " + Integer.toString(error.getCode()));
        }
    }

    @Override
    public void onTokenCreatedFail(ApiError error) {}

    @Override
    public void onTokenCreated(Token token) {}
}
