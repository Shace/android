package io.shace.app.api.listeners;

import io.shace.app.api.ApiError;
import io.shace.app.api.Listener;
import io.shace.app.api.models.User;

/**
 * Created by melvin on 8/7/14.
 */
public interface UserListener extends Listener {

    public void onUserCreated(User user);

    public void onUserUpdated(User user);

    public void onUserDeleted();

    public void onUserCreatedFail(ApiError error);

    public void onUserUpdatedFail(ApiError error);

    public void onUserDeletedFail(ApiError error);
}
