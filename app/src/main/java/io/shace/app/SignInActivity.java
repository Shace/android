package io.shace.app;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import io.shace.app.api.models.Token;
import io.shace.app.api.models.listeners.TokenListener;


@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends Activity implements TextView.OnEditorActionListener, TokenListener {
    @ViewById(R.id.icon_loader) protected ProgressBar mIconLoader;
    @ViewById(R.id.sign_in_form) View mFormView;

    @ViewById(R.id.email) protected AutoCompleteTextView mEmailView;
    @ViewById(R.id.password) protected EditText mPasswordView;

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
    public void onTokenCreated(Token token) {

    }

    @Override
    public void onTokenUpdated(Token token) {

    }

    @Override
    public void onTokenCreatedFail() {

    }

    @Override
    public void onTokenUpdatedFail() {

    }
}
