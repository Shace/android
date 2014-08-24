package io.shace.app.api.listeners;

import java.util.List;

import io.shace.app.api.ApiError;
import io.shace.app.api.Listener;
import io.shace.app.api.models.Event;

/**
 * Created by melvin on 8/14/14.
 */
public interface EventListener extends Listener {
    public void onEventsFound(List<Event> events);
    public void onEventFound(Event event);
    public void onEventCreated(Event event);
    public void onEventCreatedFail(ApiError error);
}
