package io.shace.app.ui.user;

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

import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.UserListener;
import io.shace.app.api.models.User;
import io.shace.app.tools.PreferenceTools;


@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends BaseActivity implements TextView.OnEditorActionListener, UserListener {
    public static final String TAG = SignUpActivity.class.getSimpleName();

    @ViewById(R.id.icon_loader) protected ProgressBar mIconLoader;
    @ViewById(R.id.sign_up_form) View mFormView;
    @ViewById(R.id.beta_view) View mBetaView;

    @ViewById(R.id.first_name) protected AutoCompleteTextView mFirstNameView;
    @ViewById(R.id.last_name) protected AutoCompleteTextView mLastNameView;
    @ViewById(R.id.email) protected AutoCompleteTextView mEmailView;
    @ViewById(R.id.password) protected EditText mPasswordView;


    @AfterViews
    protected void init() {
        mPasswordView.setOnEditorActionListener(this);
        displayCorrectPage();
    }

    private void displayCorrectPage() {
        if (PreferenceTools.getKey(PreferenceTools.KEY_BETA_STATUS, false)) {
            mBetaView.setVisibility(View.VISIBLE);
            mFormView.setVisibility(View.GONE);
        }
    }

    @Click(R.id.signUpButton)
    protected void signUp() {
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        User user = new User(email, password, firstName, lastName);
        user.save(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
        if (actionID == EditorInfo.IME_ACTION_DONE) {
            signUp();
        }
        return true;
    }

    @Override
    public void onUserCreated(User user) {
        setBetaInfo();
    }

    @Override
    public void onUserCreatedFail(ApiError error) {
        if (error.is(ApiError.PARAMETERS_ERROR)) {
            HashMap<String, TextView> fields = new HashMap<String, TextView>();
            fields.put("first_name", mFirstNameView);
            fields.put("last_name", mLastNameView);
            fields.put("email", mEmailView);
            fields.put("password", mPasswordView);

            checkFormError(error, fields);
        } else if (error.is(ApiError.BETA_PROCESSING)) {
            setBetaInfo();
        }
    }

    private void setBetaInfo() {
        PreferenceTools.putKey(PreferenceTools.KEY_BETA_STATUS, true);
        displayCorrectPage();
    }

    @Override
    public void onPreExecute() {
        mFormView.setVisibility(View.GONE);
        mIconLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute() {
        mIconLoader.setVisibility(View.GONE);

        if (PreferenceTools.getKey(PreferenceTools.KEY_BETA_STATUS, false) == false) {
            mFormView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserUpdated(User user) {}

    @Override
    public void onUserDeleted() {}

    @Override
    public void onUserUpdatedFail(ApiError error) {}

    @Override
    public void onUserDeletedFail(ApiError error) {}
}
