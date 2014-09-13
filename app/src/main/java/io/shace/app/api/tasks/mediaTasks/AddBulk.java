package io.shace.app.api.tasks.mediaTasks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.shace.app.api.ApiError;
import io.shace.app.api.Routes;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.MediaListener;
import io.shace.app.api.models.Media;
import io.shace.app.api.network.ApiCall;
import io.shace.app.api.network.RequestQueue;

/**
 * Created by melvin on 8/7/14.
 */
public class AddBulk extends Task {
    private static final String TAG = AddBulk.class.getSimpleName();
    protected MediaListener mListener;

    public AddBulk(MediaListener listener) {
        mListener = listener;
        setGenericListener(listener);

        setAllowedCodes(new int[] {400, 401});
    }

    public void exec() {
        cancel();
        new ApiCall(TAG).post(Routes.MEDIAS, mData, mJsonData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            JSONArray jsonMedias = response.getJSONArray("medias");
            List<Media> medias = Media.fromJson(jsonMedias);
            mListener.onMediasGenerated(medias);
        } catch (JSONException e) {
            Log.e(TAG, "The key 'medias' has not been found in " + response.toString());
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        ApiError error = getError(response);
        if (error != null) {
            mListener.onMediasGeneratedFail(error);
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
