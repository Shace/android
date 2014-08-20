package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Comment;

/**
 * Created by melvin on 8/16/14.
 */
public class CommentListDeserializer implements JsonDeserializer<List<Comment>> {
    private static final String TAG = CommentListDeserializer.class.getSimpleName();

    private DeserializerBuilder<List<Comment>> builder = new DeserializerBuilder<List<Comment>>();

    @Override
    public List<Comment> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.COMMENT_LIST);
        builder.handleComment();
        return builder.buildFromJson(json);
    }
}
