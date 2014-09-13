package io.shace.app.api.serialization.models;

import java.util.List;

import io.shace.app.api.models.Event;
import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/16/14.
 */
public class EventList extends Serialization<List<Event>> {
    private static final String TAG = EventList.class.getSimpleName();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.EVENT_LIST);
        builder.handleEvent();
    }
}
