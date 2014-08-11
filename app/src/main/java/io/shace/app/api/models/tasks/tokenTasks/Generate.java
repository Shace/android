package io.shace.app.api.models.tasks.tokenTasks;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONObject;

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
public class Generate extends Task {
    private static final String TAG = Generate.class.getSimpleName();
    protected TokenListener mListener;

    public Generate(TokenListener listener) {
        mListener = listener;
        setGenericListener(listener);

        setAllowedCodes(new int[] {401});
    }

    public void exec() {
        new AsyncApiCall().post(Routes.ACCESS_TOKEN, this.mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            Token token = jsonObjectToObject(response, Token.class);
            token.save();
            mListener.onTokenCreated(token);

            // Todo move to the caller
                //redirectToHomepage();
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
