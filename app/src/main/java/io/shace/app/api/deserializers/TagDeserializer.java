package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Tag;

/**
 * Created by melvin on 8/15/14.
 */
public class TagDeserializer implements JsonDeserializer<Tag> {
    private static final String TAG = TagDeserializer.class.getSimpleName();
    private DeserializerBuilder<Tag> builder = new DeserializerBuilder<Tag>();

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.TAG);
        builder.handleTagList();
        return builder.buildFromJson(json);
    }
}
