package io.shace.app.api.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.models.Image;

/**
 * Created by melvin on 8/15/14.
 */
public class ImageDeserializer implements JsonDeserializer<Image> {
    private static final String TAG = ImageDeserializer.class.getSimpleName();
    private DeserializerBuilder<Image> builder = new DeserializerBuilder<Image>();

    @Override
    public Image deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        builder.setMainType(DeserializerBuilder.Type.IMAGE);
        return builder.buildFromJson(json);
    }
}
