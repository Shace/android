package io.shace.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.shace.app.api.ApiResponse;
import io.shace.app.api.AsyncApiCall;
import io.shace.app.api.Routes;
import io.shace.app.tools.ToastTools;

@EFragment(R.layout.fragment_sign_in)
public class SignInFragment extends Fragment {
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
        mFormView.setVisibility(View.GONE);
        mIconLoader.setVisibility(View.VISIBLE);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        HashMap<String,String> postData = new HashMap<String, String>();
        postData.put("email", email);
        postData.put("password", password);
        postData.put("auto_renew", "true");

        new AsyncApiCall(getApplicationContext()).post(Routes.ACCESS_TOKEN, postData,
                new ApiResponse(new int[]{401}) {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.i(TAG, "Connected!");

                        try {
                            SharedPreferences.Editor settings = getActivity().getSharedPreferences("settings", Context.MODE_APPEND).edit();
                            settings.putString("accessToken", response.getString("token"));
                            settings.putInt("userId", response.getInt("user_id"));
                            settings.putLong("creation", response.getLong("creation"));
                            settings.putLong("expiration", response.getLong("expiration"));
                            settings.apply();
                            redirectToHomepage();
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            ToastTools.use().longToast(getApplicationContext(), R.string.internal_error);
                        }
                    }

                    @Override
                    public void onError(int errorCode, JSONObject response) {
                        Log.v(TAG, response.toString());
                        ToastTools.use().longToast(getApplicationContext(), R.string.error_sign_in);
                    }

                    @Override
                    public void onError(int errorCode, String response) {
                        Log.v(TAG, response);
                        ToastTools.use().longToast(getApplicationContext(), R.string.error_sign_in);
                    }

                    @Override
                    protected void alwaysAfter() {
                        mIconLoader.setVisibility(View.GONE);
                        mFormView.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    @Click
    void signUpButton() {
        mFormView.setVisibility(View.GONE);
        mIconLoader.setVisibility(View.VISIBLE);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        HashMap<String,String> putData = new HashMap<String, String>();
        putData.put("email", email);
        putData.put("password", password);
        putData.put("first_name", password);
        putData.put("last_name", password);
        putData.put("birth_date", password);

        new AsyncApiCall(getApplicationContext()).post(Routes.USERS, putData,
                new ApiResponse(new int[]{400}) {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            SharedPreferences.Editor settings = getActivity().getSharedPreferences("settings", Context.MODE_APPEND).edit();
                            settings.putInt("userId", response.getInt("id"));
                            settings.apply();
                            redirectToHomepage();

                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            ToastTools.use().longToast(getApplicationContext(), R.string.internal_error);
                        }
                    }

                    @Override
                    public void onError(int errorCode, JSONObject response) {
                        Log.v(TAG, response.toString());
                        ToastTools.use().longToast(getApplicationContext(), R.string.error_sign_up);
                    }

                    @Override
                    public void onError(int errorCode, String response) {
                        Log.v(TAG, response);
                        ToastTools.use().longToast(getApplicationContext(), R.string.error_sign_up);
                    }

                    @Override
                    protected void alwaysAfter() {
                        mIconLoader.setVisibility(View.GONE);
                        mFormView.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
}