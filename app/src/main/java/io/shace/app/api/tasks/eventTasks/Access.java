package io.shace.app.api.tasks.eventTasks;

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
public class Access extends Task {
    private static final String TAG = Access.class.getSimpleName();
    private EventListener mListener;

    public Access(EventListener listener) {
        mListener = listener;
        setGenericListener(listener);
        setAllowedCodes(new int[] {400, 403, 404});
    }

    @Override
    public void exec() {
        cancel();
        new ApiCall(TAG).post(Routes.EVENTS_ACCESS, mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        Event event = Event.fromJson(response);
        mListener.onEventRetrieved(event);
    }

    @Override
    public void onError(int code, JSONObject response) {
        ApiError error = getError(response);

        mListener.onEventWrongPassword(error);
    }

    public static void cancel() {
        RequestQueue.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void public_static_void_cancel() {
        cancel();
    }
}
