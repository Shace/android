package io.shace.app.api.network.requests;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by melvin on 9/9/14.
 */
abstract public class ApiRequest<T> extends Request<T> {
    private static final String TAG = ApiRequest.class.getSimpleName();

    long mCreationDate = 0;

    public ApiRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        setCreationDate();
    }

    public long getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate() {
        mCreationDate = System.currentTimeMillis();
    }
}
