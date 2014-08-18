package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Event;

/**
 * Created by melvin on 8/15/14.
 */
public class EventDeserializer implements JsonDeserializer<Event> {
    private static final String TAG = EventDeserializer.class.getSimpleName();
    private DeserializerBuilder<Event> builder = new DeserializerBuilder<Event>();

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.EVENT);
        builder.handleMediaList();
        return builder.buildFromJson(json);
    }
}
