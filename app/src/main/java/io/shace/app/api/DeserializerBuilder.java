package io.shace.app.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.api.deserializers.EventDeserializer;
import io.shace.app.api.deserializers.EventListDeserializer;
import io.shace.app.api.deserializers.MediaListDeserializer;
import io.shace.app.api.deserializers.UserListDeserializer;
import io.shace.app.api.deserializers.MediaDeserializer;
import io.shace.app.api.deserializers.TokenDeserializer;
import io.shace.app.api.deserializers.UserDeserializer;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.api.models.Token;
import io.shace.app.api.models.User;

/**
 * Usage:
 *
 * <code>
 *     // One big line constructor
 *     DeserializerBuilder<List<Event>> builder = new DeserializerBuilder<List<Event>>(DeserializerBuilder.Type.EVENT_LIST);
 *
 *     // Two lines constructor
 *     DeserializerBuilder<List<Event>> builder = new DeserializerBuilder<List<Event>>();
 *     builder.setMainType(DeserializerBuilder.Type.EVENT_LIST);
 *
 *     builder.handleMediaList(); // Add support for medias
 *     List<Event> = builder.fromJson(json); // build the object
 * </code>
 */
public class DeserializerBuilder<T> {
    private static final String TAG = DeserializerBuilder.class.getSimpleName();

    private Map<Type, java.lang.reflect.Type> mTypeToJavaType = new HashMap<Type, java.lang.reflect.Type>();
    private Map<Type, Class<?>> mTypeToDeserializer = new HashMap<Type, Class<?>>();

    private GsonBuilder mBuilder = new GsonBuilder();
    private Type mType = null;

    public enum Type {
        EVENT,
        EVENT_LIST,
        MEDIA,
        MEDIA_LIST,
        TOKEN,
        USER,
        USER_LIST,
    }

    private void setDeserializers() {
        mTypeToDeserializer.put(Type.EVENT, EventDeserializer.class);
        mTypeToDeserializer.put(Type.EVENT_LIST, EventListDeserializer.class);
        mTypeToDeserializer.put(Type.MEDIA, MediaDeserializer.class);
        mTypeToDeserializer.put(Type.MEDIA_LIST, MediaListDeserializer.class);
        mTypeToDeserializer.put(Type.TOKEN, TokenDeserializer.class);
        mTypeToDeserializer.put(Type.USER, UserDeserializer.class);
        mTypeToDeserializer.put(Type.USER_LIST, UserListDeserializer.class);
    }

    private void setJavaTypes() {
        mTypeToJavaType.put(Type.EVENT, Event.class);
        mTypeToJavaType.put(Type.EVENT_LIST, new TypeToken<List<Event>>() {}.getType());
        mTypeToJavaType.put(Type.MEDIA, Event.class);
        mTypeToJavaType.put(Type.MEDIA_LIST, new TypeToken<List<Media>>() {}.getType());
        mTypeToJavaType.put(Type.TOKEN, Token.class);
        mTypeToJavaType.put(Type.USER, User.class);
        mTypeToJavaType.put(Type.USER_LIST, new TypeToken<List<User>>() {}.getType());
    }

    protected void handle(Type type) {
        Class<?> deserializer = mTypeToDeserializer.get(type);
        java.lang.reflect.Type javaType = mTypeToJavaType.get(type);

        if (deserializer == null || javaType == null) {
            Log.e(TAG, "Type '" + type + "' not registered everywhere");
        } else {
            try {
                Constructor<?> ctor = deserializer.getConstructor();
                mBuilder.registerTypeAdapter(javaType, ctor.newInstance());
            } catch (Exception e) {
                Log.e(TAG, "Fail: " + e.getMessage());
            }
        }
    }

    public DeserializerBuilder(Type type) {
        setJavaTypes();
        setDeserializers();
        mType = type;
    }

    public DeserializerBuilder() {
        setJavaTypes();
        setDeserializers();
    }

    /**
     * Return the Gson object
     *
     * @return the Gson object
     */
    public Gson build() {
        return mBuilder.create();
    }

    /**
     *
     * @param json string containing the data
     *
     * @return an instance of T
     */
    public T buildFromJson(String json) {
        java.lang.reflect.Type javaType = mTypeToJavaType.get(mType);
        return build().fromJson(json, javaType);
    }

    /**
     *
     * @param json JsonElement containing the data
     *
     * @return an instance of T
     */
    public T buildFromJson(JsonElement json) {
        java.lang.reflect.Type javaType = mTypeToJavaType.get(mType);
        return build().fromJson(json, javaType);
    }

    /**
     * Set given type as main type
     *
     */
    public void setMainType(Type type) {
        mType = type;
    }

    /**
     * Add support for Event
     */
    public void handleEvent() {
        handle(Type.EVENT);
    }

    /**
     * Add support for List<Event>
     */
    public void handleEventList() {
        handle(Type.EVENT_LIST);
    }

    /**
     * Add support for Media
     */
    public void handleMedia() {
        handle(Type.MEDIA);
    }

    /**
     * Add support for List<Media>
     */
    public void handleMediaList() {
        handle(Type.MEDIA_LIST);
    }

    /**
     * Add support for Token
     */
    public void handleToken() {
        handle(Type.TOKEN);
    }

    /**
     * Add support for User
     */
    public void handleUser() {
        handle(Type.USER);
    }

    /**
     * Add support for List<User>
     */
    public void handleUserList() {
        handle(Type.USER_LIST);
    }
}
