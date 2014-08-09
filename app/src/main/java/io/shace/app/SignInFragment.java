package io.shace.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

import io.shace.app.api.models.Token;
import io.shace.app.api.models.listeners.TokenListener;

// todo move signup form
@EFragment(R.layout.fragment_sign_in)
public class SignInFragment extends Fragment implements TokenListener {
    private static final String TAG = "SignInFragment";

    @ViewById(R.id.formLayout) View mFormView;
    @ViewById(R.id.iconLoader) ProgressBar mIconLoader;

    @ViewById(R.id.fieldEmail) AutoCompleteTextView mEmailView;
    @ViewById(R.id.fieldPassword) EditText mPasswordView;

    public SignInFragment() {
    }

    private android.content.Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    // TODO: Put in a tool
    private void redirectToHomepage() {
        Fragment fragment = new HomepageFragment_();

        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Click
    void signInButton() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        Token.generate(this);
    }

    @Click
    void signUpButton() {
        mFormView.setVisibility(View.GONE);
        mIconLoader.setVisibility(View.VISIBLE);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

//        User user = new User(email, password, "", "", new Date());
//        user.save();


        HashMap<String,String> putData = new HashMap<String, String>();
        putData.put("email", email);
        putData.put("password", password);
        putData.put("first_name", password);
        putData.put("last_name", password);
        putData.put("birth_date", password);

//        new AsyncApiCall().post(Routes.USERS, putData,
//                new ApiResponseCallbacks(new int[]{400, 403}) {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        try {
//                            SharedPreferences.Editor settings = getActivity().getSharedPreferences("settings", Context.MODE_APPEND).edit();
//                            settings.putInt("userId", response.getInt("id"));
//                            settings.apply();
//                            redirectToHomepage();
//
//                        } catch (JSONException e) {
//                            Log.e(TAG, e.getMessage());
//                            ToastTools.use().longToast(R.string.internal_error);
//                        }
//                    }
//
//                    @Override
//                    public void onError(int errorCode, JSONObject response) {
//                        Log.v(TAG, response.toString());
//                        ToastTools.use().longToast(R.string.error_sign_up);
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String response) {
//                        Log.v(TAG, response);
//                        ToastTools.use().longToast(response);
//                    }
//
//                    @Override
//                    protected void alwaysAfter() {
//                        mIconLoader.setVisibility(View.GONE);
//                        mFormView.setVisibility(View.VISIBLE);
//                    }
//                }
//        );
    }

    @Override
    public void onTokenCreated(Token token) {
        Log.i(TAG, "Created: " + token.getToken());
    }

    @Override
    public void onTokenUpdated(Token token) {
        Log.i(TAG, "Updated: " + token.getToken());
    }

    @Override
    public void onTokenCreatedFail() {
        Log.i(TAG, "Creation FAILED");
    }

    @Override
    public void onTokenUpdatedFail() {
        Log.i(TAG, "Update FAILED");
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
}