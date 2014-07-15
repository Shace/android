package io.shace.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;

import io.shace.app.api.ApiResponse;
import io.shace.app.api.AsyncApiCall;
import io.shace.app.api.Routes;
import io.shace.app.tools.ToastTools;

@EFragment(R.layout.fragment_home_page)
public class Homepage extends Fragment {
    private static final String TAG = "Homepage";

    @ViewById(R.id.formLayout) View mFormView;
    @ViewById(R.id.iconLoader) ProgressBar mIconLoader;


    @ViewById(R.id.fieldEmail) AutoCompleteTextView mEmailView;
    @ViewById(R.id.fieldPassword) EditText mPasswordView;

    public Homepage() {
    }

    private android.content.Context getApplicationContext() {
        return getActivity().getApplicationContext();
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

        new AsyncApiCall(getApplicationContext()).post(Routes.ACCESS_TOKEN, postData,
                new ApiResponse(new int[]{401}) {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.i(TAG, "Connected!");

                        try {
                            SharedPreferences.Editor settings = getActivity().getSharedPreferences("Settings", Context.MODE_APPEND).edit();
                            settings.putString("accessToken", response.getString("token"));
                            settings.putInt("creation", response.getInt("user_id"));
                            settings.putLong("creation", response.getLong("creation"));
                            settings.putLong("expiration", response.getLong("expiration"));
                            settings.apply();

//                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
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

        new AsyncApiCall(getApplicationContext()).put(Routes.USERS, putData,
                new ApiResponse(new int[]{400}) {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.i(TAG, "Created!");

                        try {
                            SharedPreferences.Editor settings = getActivity().getSharedPreferences("Settings", Context.MODE_APPEND).edit();
                            settings.putString("accessToken", response.getString("token"));
                            settings.putInt("creation", response.getInt("user_id"));
                            settings.putLong("creation", response.getLong("creation"));
                            settings.putLong("expiration", response.getLong("expiration"));
                            settings.apply();

//                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
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