package io.shace.app.api;

import org.json.JSONObject;

/**
 * Created by melvin on 5/5/14.
 *
 * The interface is used as callbacks in all api calls
 */
public interface ApiResponseCallbacks {

    /**
     * This function must returns a list containing the HTTP error code handled by the user
     *
     * @return list of allowed codes;
     */
    public int[] getAllowedCodes();

    /**
     * Callback called when the request has succeed
     *
     * @param response Json response from the API
     */
    public void onSuccess(JSONObject response);

    /**
     * Callback called when the request has failed, and when the returned HTTP code match {@link io.shace.app.api.models.tasks.Task#mAllowedCodes}
     *
     * @param code HTTP code return by the server
     * @param response return by the API
     */
    public void onError(int code, JSONObject response);

    /**
     * see {@link ApiResponseCallbacks#onError(int, org.json.JSONObject)}
     *
     * This method is called when the response is not a valid JSON document
     *
     * @param code HTTP code return by the server
     * @param response return by the API
     */
    public void onError(int code, String response);

    /**
     * Callback always called at the very beginning of the response
     */
    public void alwaysBefore();

    /**
     * Callback always called at the very end of the response
     */
    public void alwaysAfter();
}