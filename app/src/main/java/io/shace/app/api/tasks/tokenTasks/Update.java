package io.shace.app.api.tasks.tokenTasks;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONObject;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.network.ApiCall;
import io.shace.app.api.Routes;
import io.shace.app.api.models.Token;
import io.shace.app.api.listeners.TokenListener;
import io.shace.app.api.Task;
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

        setAllowedCodes(new int[] {401, 404});
    }

    public void exec() {
        new ApiCall().put(Routes.ACCESS_TOKEN, mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            Token token = Token.fromJson(response);
            token.save();
            mListener.onTokenUpdated(token);
        } catch (JsonParseException e) {
            Log.e(TAG, e.getMessage());
            ToastTools.use().longToast(R.string.internal_error);
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        ApiError error = getError(response);
        if (error != null) {
            mListener.onTokenUpdatedFail(error);
        }
    }
}
