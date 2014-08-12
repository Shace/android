package io.shace.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import java.util.Date;
import java.util.HashMap;

import io.shace.app.api.ApiError;
import io.shace.app.api.models.User;
import io.shace.app.api.listeners.UserListener;


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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());

        if (pref.getBoolean("beta.status", false)) {
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

        User user = new User(email, password, firstName, lastName, new Date());
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
            fields.put("firstName", mFirstNameView);
            fields.put("lastName", mLastNameView);
            fields.put("email", mEmailView);
            fields.put("password", mPasswordView);

            checkFormError(error, fields);
        } else if (error.is(ApiError.BETA_PROCESSING)) {
            setBetaInfo();
        }
    }

    private void setBetaInfo() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("beta.status", true);
        editor.apply();
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

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        if (pref.getBoolean("beta.status", false) == false) {
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
