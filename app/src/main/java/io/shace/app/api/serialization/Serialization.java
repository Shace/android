package io.shace.app.api.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by melvin on 9/12/14.
 */
abstract public class Serialization<T> implements JsonDeserializer<T>, JsonSerializer<T> {
    protected TypeBuilder<T> builder = new TypeBuilder<T>();

    abstract protected void build();

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        build();
        return builder.buildFromJson(json);
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        build();
        return builder.toJson(src);
    }
}
