package io.shace.app.api.models.tasks.userTasks;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.AsyncApiCall;
import io.shace.app.api.Routes;
import io.shace.app.api.models.Token;
import io.shace.app.api.models.User;
import io.shace.app.api.models.listeners.UserListener;
import io.shace.app.api.models.tasks.Task;
import io.shace.app.api.models.tasks.tokenTasks.Generate;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 8/9/14.
 */
public class Add extends Task {
    private static final String TAG = Generate.class.getSimpleName();
    protected UserListener mListener;

    public Add(UserListener listener) {
        mListener = listener;
        setGenericListener(listener);

        setAllowedCodes(new int[] {400, 403});
    }

    @Override
    public void exec(Map<String, String> data) {
        new AsyncApiCall().post(Routes.USERS, data, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            User user = jsonObjectToModel(response, User.class);

            Token token = Token.get();
            token.setUserId(user.getId());
            token.save();

            mListener.onUserCreated(user);
        } catch (JsonParseException e) {
            Log.e(TAG, e.getMessage());
            ToastTools.use().longToast(R.string.internal_error);
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        Log.v(TAG, response.toString());
        ToastTools.use().longToast(R.string.error_sign_in);
    }

    @Override
    public void onError(int code, String response) {
        Log.v(TAG, response);
        ToastTools.use().longToast(response);
    }
}
