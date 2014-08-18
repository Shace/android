package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.User;

/**
 * Created by melvin on 8/15/14.
 */
public class UserDeserializer implements JsonDeserializer<User> {
    private static final String TAG = UserDeserializer.class.getSimpleName();
    private DeserializerBuilder<User> builder = new DeserializerBuilder<User>();

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.USER);
        return builder.buildFromJson(json);
    }
}
