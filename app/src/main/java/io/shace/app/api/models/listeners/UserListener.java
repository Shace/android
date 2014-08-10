package io.shace.app.api.models.listeners;

import io.shace.app.api.models.User;

/**
 * Created by melvin on 8/7/14.
 */
public interface UserListener extends Listener {

    public void onUserCreated(User user);

    public void onUserUpdated(User user);

    public void onUserDeleted();

    public void onUserCreatedFail();

    public void onUserUpdatedFail();

    public void onUserDeletedFail();
}
