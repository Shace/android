package io.shace.app.api.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import io.shace.app.App;

/**
 * Created by melvin on 4/30/14.
 */
public class RequestQueue {
    private static final String TAG = RequestQueue.class.getSimpleName();

    private com.android.volley.RequestQueue mRequestQueue = null;
    private static RequestQueue sInstance = null;

    private RequestQueue() {
    }

    public static synchronized RequestQueue getInstance() {
        if (sInstance == null) {
            sInstance = new RequestQueue();
        }

        return sInstance;
    }

    public com.android.volley.RequestQueue get() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(App.getContext());
        }

        return mRequestQueue;
    }

    public <T> void add(Request<T> req, Object tag) {
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        req.setTag(tag);
        get().add(req);
    }

    /**
     * Cancel all the pending requests
     *
     * @param tag tag attached to the request
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Cancel all the pending requests
     */
    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(new com.android.volley.RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    try {
                        Map<String, String> str = request.getHeaders();

                        for (Map.Entry<String, String> entry : str.entrySet()) {
                            Log.e(TAG, entry.getKey() + ": " + entry.getValue());
                        }

                    } catch (AuthFailureError authFailureError) {
                        //authFailureError.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }
}
