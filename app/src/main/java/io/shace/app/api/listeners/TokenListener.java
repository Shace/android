package io.shace.app.api.listeners;

import io.shace.app.api.ApiError;
import io.shace.app.api.Listener;
import io.shace.app.api.models.Token;

/**
 * Created by melvin on 8/7/14.
 */
public interface TokenListener extends Listener {
    /**
     * Callback called when the token has been created
     */
    public void onTokenCreated(Token token);

    /**
     * Callback called when the token has been updated
     */
    public void onTokenUpdated(Token token);

    /**
     * Callback called when the creation of the token has failed
     */
    public void onTokenCreatedFail(ApiError error);

    /**
     * Callback called when the update of the token has failed
     */
    public void onTokenUpdatedFail(ApiError error);
}
