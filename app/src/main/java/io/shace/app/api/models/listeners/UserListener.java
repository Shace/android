package io.shace.app.api.models.listeners;

/**
 * Created by melvin on 8/7/14.
 */
public interface UserListener extends Listener {

    public void onUserCreated();

    public void onUserUpdated();

    public void onUserDeleted();

    public void onUserCreatedFail();

    public void onUserUpdatedFail();

    public void onUserDeletedFail();
}
