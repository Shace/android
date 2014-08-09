package io.shace.app;

import android.app.Activity;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import io.shace.app.api.models.User;
import io.shace.app.tools.ToastTools;

@WindowFeature({
        Window.FEATURE_NO_TITLE,
        Window.FEATURE_INDETERMINATE_PROGRESS
})
@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends Activity {
    @ViewById(R.id.icon_loader) protected ProgressBar mIconLoader;

    @ViewById(R.id.first_name) protected AutoCompleteTextView mFirstNameView;
    @ViewById(R.id.last_name) protected AutoCompleteTextView mLastNameView;
    @ViewById(R.id.email) protected AutoCompleteTextView mEmailView;
    @ViewById(R.id.password) protected EditText mPasswordView;

    @Click(R.id.signUpButton)
    protected void signUp() {
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        User user = new User(email, password, firstName, lastName, null);
        //user.save(this);
        ToastTools.use().longToast("OK");
    }
}
