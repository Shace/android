package io.shace.app.api.tasks.tokenTasks;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONObject;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.Routes;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.TokenListener;
import io.shace.app.api.models.Token;
import io.shace.app.api.network.ApiCall;
import io.shace.app.api.network.RequestQueue;
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
        cancel();
        new ApiCall(TAG).post(Routes.ACCESS_TOKEN, this.mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            Token token = Token.fromJson(response);
            token.save();
            mListener.onTokenCreated(token);
        } catch (JsonParseException e) {
            Log.e(TAG, e.getMessage());
            ToastTools.use().longToast(R.string.internal_error);
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        ApiError error = getError(response);
        if (error != null) {
            mListener.onTokenCreatedFail(error);
        }
    }

    public static void cancel() {
        RequestQueue.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void public_static_void_cancel() {
        cancel();
    }
}
