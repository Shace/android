package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Media;

/**
 * Created by melvin on 8/16/14.
 */
public class MediaListDeserializer implements JsonDeserializer<List<Media>> {
    private static final String TAG = MediaListDeserializer.class.getSimpleName();
    private DeserializerBuilder<List<Media>> builder = new DeserializerBuilder<List<Media>>();

    @Override
    public List<Media> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.MEDIA_LIST);
        builder.handleMedia();
        return builder.buildFromJson(json);
    }
}

