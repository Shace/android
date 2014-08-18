package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Media;

/**
 * Created by melvin on 8/16/14.
 */
public class MediaDeserializer implements JsonDeserializer<Media> {
    private static final String TAG = EventDeserializer.class.getSimpleName();
    private DeserializerBuilder<Media> builder = new DeserializerBuilder<Media>();

    @Override
    public Media deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.MEDIA);
        return builder.buildFromJson(json);
    }
}