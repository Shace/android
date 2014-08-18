package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Event;

/**
 * Created by melvin on 8/16/14.
 */
public class EventListDeserializer implements JsonDeserializer<List<Event>> {
    private static final String TAG = EventListDeserializer.class.getSimpleName();

    private DeserializerBuilder<List<Event>> builder = new DeserializerBuilder<List<Event>>();

    @Override
    public List<Event> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.EVENT_LIST);
        builder.handleEvent();
        return builder.buildFromJson(json);
    }
}
