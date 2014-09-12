package io.shace.app.api.network;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import java.util.Date;

import io.shace.app.App;
import io.shace.app.api.network.requests.ApiRequest;

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
    public void cancelPendingRequests(final Object tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Cannot cancelAll with a null tag");
        }

        if (mRequestQueue != null) {
            final long limit = new Date().getTime();

            mRequestQueue.cancelAll(new com.android.volley.RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> req) {
                    try {
                        ApiRequest<?> request = (ApiRequest) req;
                        return request.getTag() == tag && request.getCreationDate() < limit;
                    } catch (ClassCastException e) {
                        return req.getTag() == tag;
                    }
                }
            });
        }
    }

    /**
     * Cancel all the pending requests
     */
    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            final long limit = System.currentTimeMillis();

            mRequestQueue.cancelAll(new com.android.volley.RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> req) {
                    return true;

//                    try {
//                        ApiRequest<?> request = (ApiRequest) req;
//                        return request.getCreationDate() < limit;
//                    } catch (ClassCastException e) {
//                        return true;
//                    }
                }
            });
        }
    }
}
