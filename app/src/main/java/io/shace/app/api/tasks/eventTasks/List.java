package io.shace.app.api.tasks.eventTasks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.shace.app.api.Routes;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.network.ApiCall;
import io.shace.app.api.network.RequestQueue;


public class List extends Task {
    private static final String TAG = List.class.getSimpleName();
    private EventListener mListener;

    public List(EventListener listener) {
        mListener = listener;
        setGenericListener(listener);
        setAllowedCodes(new int[] {404});
    }

    @Override
    public void exec() {
        cancel();
        new ApiCall(TAG).get(Routes.EVENT_LIST, mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            JSONArray jsonEvents = response.getJSONArray("events");
            java.util.List<Event> events = Event.fromJson(jsonEvents);
            mListener.onEventsFound(events);
        } catch (JSONException e) {
            Log.e(TAG, "No 'events' key found: " + response.toString());
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        getError(response);
    }

    public static void cancel() {
        RequestQueue.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void public_static_void_cancel() {
        cancel();
    }
}
