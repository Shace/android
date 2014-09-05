package io.shace.app.api.tasks.eventTasks;

import android.util.Log;

import org.json.JSONObject;

import io.shace.app.api.ApiError;
import io.shace.app.api.Routes;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.network.ApiCall;
import io.shace.app.api.network.RequestQueue;

/**
 * Created by melvin on 8/14/14.
 */
public class Update extends Task {
    private static final String TAG = Update.class.getSimpleName();
    private EventListener mListener;

    public Update(EventListener listener) {
        mListener = listener;
        setGenericListener(listener);
        setAllowedCodes(new int[] {400, 401, 403, 404});
    }

    @Override
    public void exec() {
        cancel();
        Log.e(TAG, "Name" + mData.get("name"));
        Log.e(TAG, "Description" + mData.get("description"));
        new ApiCall(TAG).put(Routes.EVENTS, mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        Event event = Event.fromJson(response);
        mListener.onEventUpdated(event);
    }

    @Override
    public void onError(int code, JSONObject response) {
        ApiError error = getError(response);
        mListener.onEventUpdatedFail(error);
    }

    public static void cancel() {
        RequestQueue.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void public_static_void_cancel() {
        cancel();
    }
}
