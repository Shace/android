package io.shace.app.api.models.tasks.tokenTasks;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.AsyncApiCall;
import io.shace.app.api.Routes;
import io.shace.app.api.models.Token;
import io.shace.app.api.models.listeners.TokenListener;
import io.shace.app.api.models.tasks.Task;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 8/7/14.
 */
public class Update extends Task {
    private static final String TAG = Update.class.getSimpleName();
    protected TokenListener mListener;

    public Update(TokenListener listener) {
        mListener = listener;
        setGenericListener(listener);

        setAllowedCodes(new int[] {401});
    }

    public void exec(Map<String, String> postData) {
        new AsyncApiCall().put(Routes.ACCESS_TOKEN, postData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            Token token = jsonObjectToObject(response, Token.class);
            token.save();
            mListener.onTokenUpdated(token);

            // Todo move to the caller
                //redirectToHomepage();
        } catch (JsonParseException e) {
            mListener.onTokenUpdatedFail();
            Log.e(TAG, e.getMessage());
            ToastTools.use().longToast(R.string.internal_error);
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        Log.v(TAG, response.toString());
        ToastTools.use().longToast(R.string.error_sign_in);

        mListener.onTokenUpdatedFail();
    }

    @Override
    public void onError(int code, String response) {
        Log.v(TAG, response);
        ToastTools.use().longToast(response);

        mListener.onTokenUpdatedFail();
    }
}
