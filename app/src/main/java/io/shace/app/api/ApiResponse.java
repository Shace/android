package io.shace.app.api;

import org.json.JSONObject;

/**
 * Created by melvin on 5/5/14.
 *
 * The class is used as callbacks in all api calls
 */
public class ApiResponse {
    /*
    * HTTP codes allowed in the error callback
    */
    private int[] mAllowedCode = {};

    public ApiResponse() {
    }

    public int[] getAllowedCode() {
        return mAllowedCode;
    }

    /**
     *
     * @param allowedCode see {@link ApiResponse#mAllowedCode}
     */
    public ApiResponse(int[] allowedCode) {
        this.mAllowedCode = allowedCode;
    }

    /**
     * Callback called when the request has succeed
     *
     * @param response Json response from the API
     */
    public void onSuccess(JSONObject response) {
    }

    /**
     * Callback called when the request has failed, and when the returned HTTP code match {@link ApiResponse#mAllowedCode}
     *
     * @param code HTTP code return by the server
     * @param response return by the API
     */
    public void onError(int code, JSONObject response) {
    }

    /**
     * see {@link ApiResponse#onError(int, org.json.JSONObject)}
     *
     * This method is called when the response is not a valid JSON document
     *
     * @param code HTTP code return by the server
     * @param response return by the API
     */
    public void onError(int code, String response) {
    }

    /**
     * Callback always called at the very beginning of the response
     */
    protected void alwaysBefore() {
    }

    /**
     * Callback always called at the very end of the response
     */
    protected void alwaysAfter() {
    }
}