package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Tag;

/**
 * Created by melvin on 8/16/14.
 */
public class TagListDeserializer implements JsonDeserializer<List<Tag>> {
    private static final String TAG = TagListDeserializer.class.getSimpleName();

    private DeserializerBuilder<List<Tag>> builder = new DeserializerBuilder<List<Tag>>();

    @Override
    public List<Tag> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.TAG_LIST);
        builder.handleEvent();
        return builder.buildFromJson(json);
    }
}
