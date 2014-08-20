package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Comment;

/**
 * Created by melvin on 8/15/14.
 */
public class CommentDeserializer implements JsonDeserializer<Comment> {
    private static final String TAG = CommentDeserializer.class.getSimpleName();
    private DeserializerBuilder<Comment> builder = new DeserializerBuilder<Comment>();

    @Override
    public Comment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.COMMENT);
        return builder.buildFromJson(json);
    }
}
