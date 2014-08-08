package io.shace.app.api.models;

import org.json.JSONObject;

import io.shace.app.api.ApiResponseCallbacks;

/**
 * Created by melvin on 8/8/14.
 */
public class EmptyApiResponse implements ApiResponseCallbacks {
    @Override
    public int[] getAllowedCodes() {
        return new int[0];
    }

    @Override
    public void onSuccess(JSONObject response) {

    }

    @Override
    public void onError(int code, JSONObject response) {

    }

    @Override
    public void onError(int code, String response) {

    }

    @Override
    public void alwaysBefore() {

    }

    @Override
    public void alwaysAfter() {

    }
}
