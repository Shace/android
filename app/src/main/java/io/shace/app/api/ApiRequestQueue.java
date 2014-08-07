package io.shace.app.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import io.shace.app.App;

/**
 * Created by melvin on 4/30/14.
 */
public class ApiRequestQueue {
    private static final String TAG = "ApiRequestQueue";

    private RequestQueue mRequestQueue = null;
    private static ApiRequestQueue sInstance = null;


    private ApiRequestQueue() {
    }


    public static synchronized ApiRequestQueue getInstance() {
        if (sInstance == null) {
            sInstance = new ApiRequestQueue();
        }

        return sInstance;
    }

    public RequestQueue get() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(App.getContext());
        }

        return mRequestQueue;
    }

    public <T> void add(Request<T> req, String tag) {
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        req.setTag(tag);
        get().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
