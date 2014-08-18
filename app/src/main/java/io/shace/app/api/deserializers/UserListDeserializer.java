package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.User;

/**
 * Created by melvin on 8/16/14.
 */
public class UserListDeserializer implements JsonDeserializer<List<User>> {
    private static final String TAG = UserListDeserializer.class.getSimpleName();
    private DeserializerBuilder<List<User>> builder = new DeserializerBuilder<List<User>>();

    @Override
    public List<User> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.USER_LIST);
        builder.handleUser();
        return builder.buildFromJson(json);
    }
}

