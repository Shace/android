package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Token;

/**
 * Created by melvin on 8/15/14.
 */
public class TokenDeserializer implements JsonDeserializer<Token> {
    private static final String TAG = TokenDeserializer.class.getSimpleName();
    private DeserializerBuilder<Token> builder = new DeserializerBuilder<Token>();

    @Override
    public Token deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.TOKEN);
        return builder.buildFromJson(json);
    }
}
