package io.shace.app.api.tasks.eventTasks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.shace.app.api.Routes;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.network.ApiCall;

/**
 * Created by melvin on 8/14/14.
 */
public class Search extends Task {
    private static final String TAG = Search.class.getSimpleName();
    private EventListener mListener;

    public Search(EventListener listener) {
        mListener = listener;
        setGenericListener(listener);
        setAllowedCodes(new int[] {404});
    }

    @Override
    public void exec() {
        new ApiCall().get(Routes.EVENT_SEARCH, mData, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            JSONArray jsonEvents = response.getJSONArray("events");
            List<Event> events = Event.fromJson(jsonEvents);
            mListener.onEventsFound(events);
        } catch (JSONException e) {
            Log.e(TAG, "No event key found: " + response.toString());
        }
    }

    @Override
    public void onError(int code, JSONObject response) {
        getError(response);
    }
}
