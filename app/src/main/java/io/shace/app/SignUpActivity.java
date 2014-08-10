package io.shace.app;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.shace.app.api.models.User;
import io.shace.app.api.models.listeners.UserListener;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends Activity implements TextView.OnEditorActionListener, UserListener {
    @ViewById(R.id.icon_loader) protected ProgressBar mIconLoader;

    @ViewById(R.id.first_name) protected AutoCompleteTextView mFirstNameView;
    @ViewById(R.id.last_name) protected AutoCompleteTextView mLastNameView;
    @ViewById(R.id.email) protected AutoCompleteTextView mEmailView;
    @ViewById(R.id.password) protected EditText mPasswordView;


    @AfterViews
    protected void init() {
        mPasswordView.setOnEditorActionListener(this);
    }

    @Click(R.id.signUpButton)
    protected void signUp() {
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        User user = new User(email, password, firstName, lastName, null);
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

    }

    @Override
    public void onUserUpdated(User user) {

    }

    @Override
    public void onUserDeleted() {

    }

    @Override
    public void onUserCreatedFail() {

    }

    @Override
    public void onUserUpdatedFail() {

    }

    @Override
    public void onUserDeletedFail() {

    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute() {

    }
}
