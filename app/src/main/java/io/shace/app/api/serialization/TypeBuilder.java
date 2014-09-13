package io.shace.app.api.serialization;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.api.serialization.models.Comment;
import io.shace.app.api.serialization.models.CommentList;
import io.shace.app.api.serialization.models.Event;
import io.shace.app.api.serialization.models.EventList;
import io.shace.app.api.serialization.models.Image;
import io.shace.app.api.serialization.models.Media;
import io.shace.app.api.serialization.models.MediaList;
import io.shace.app.api.serialization.models.Tag;
import io.shace.app.api.serialization.models.TagList;
import io.shace.app.api.serialization.models.Token;
import io.shace.app.api.serialization.models.User;
import io.shace.app.api.serialization.models.UserList;

/**
 * Usage:
 *
 * <code>
 *     // One big line constructor
 *     TypeBuilder<List<Event>> builder = new TypeBuilder<List<Event>>(TypeBuilder.Type.EVENT_LIST);
 *
 *     // Two lines constructor
 *     TypeBuilder<List<Event>> builder = new TypeBuilder<List<Event>>();
 *     builder.setMainType(TypeBuilder.Type.EVENT_LIST);
 *
 *     builder.handleMediaList(); // Add support for medias
 *     List<Event> = builder.fromJson(json); // build the object
 * </code>
 */
public class TypeBuilder<T> {
    private static final String TAG = TypeBuilder.class.getSimpleName();

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
        COMMENT,
        COMMENT_LIST,
        TAG,
        TAG_LIST,
        IMAGE,
    }

    private void setDeserializers() {
        mTypeToDeserializer.put(Type.EVENT, Event.class);
        mTypeToDeserializer.put(Type.EVENT_LIST, EventList.class);
        mTypeToDeserializer.put(Type.MEDIA, Media.class);
        mTypeToDeserializer.put(Type.MEDIA_LIST, MediaList.class);
        mTypeToDeserializer.put(Type.TOKEN, Token.class);
        mTypeToDeserializer.put(Type.USER, User.class);
        mTypeToDeserializer.put(Type.USER_LIST, UserList.class);
        mTypeToDeserializer.put(Type.COMMENT, Comment.class);
        mTypeToDeserializer.put(Type.COMMENT_LIST, CommentList.class);
        mTypeToDeserializer.put(Type.TAG, Tag.class);
        mTypeToDeserializer.put(Type.TAG_LIST, TagList.class);
        mTypeToDeserializer.put(Type.IMAGE, Image.class);
    }

    private void setJavaTypes() {
        mTypeToJavaType.put(Type.EVENT, io.shace.app.api.models.Event.class);
        mTypeToJavaType.put(Type.EVENT_LIST, new TypeToken<List<io.shace.app.api.models.Event>>() {}.getType());
        mTypeToJavaType.put(Type.MEDIA, io.shace.app.api.models.Event.class);
        mTypeToJavaType.put(Type.MEDIA_LIST, new TypeToken<List<io.shace.app.api.models.Media>>() {}.getType());
        mTypeToJavaType.put(Type.TOKEN, io.shace.app.api.models.Token.class);
        mTypeToJavaType.put(Type.USER, io.shace.app.api.models.User.class);
        mTypeToJavaType.put(Type.USER_LIST, new TypeToken<List<io.shace.app.api.models.User>>() {}.getType());
        mTypeToJavaType.put(Type.COMMENT, io.shace.app.api.models.Comment.class);
        mTypeToJavaType.put(Type.COMMENT_LIST, new TypeToken<List<io.shace.app.api.models.Comment>>() {}.getType());
        mTypeToJavaType.put(Type.TAG, io.shace.app.api.models.Tag.class);
        mTypeToJavaType.put(Type.TAG_LIST, new TypeToken<List<io.shace.app.api.models.Tag>>() {}.getType());
        mTypeToJavaType.put(Type.IMAGE, io.shace.app.api.models.Image.class);
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

    public TypeBuilder(Type type) {
        setJavaTypes();
        setDeserializers();
        mType = type;
    }

    public TypeBuilder() {
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
     *
     * @param json string containing the data
     *
     * @return a json string
     */
    public JsonElement toJson(T json) {
        mBuilder.excludeFieldsWithoutExposeAnnotation();
        mBuilder.setDateFormat(DateFormat.LONG);

        java.lang.reflect.Type javaType = mTypeToJavaType.get(mType);
        return build().toJsonTree(json);
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

    /**
     * Add support for Comment
     */
    public void handleComment() {
        handle(Type.COMMENT);
    }

    /**
     * Add support for List<Comment>
     */
    public void handleCommentList() {
        handle(Type.COMMENT_LIST);
    }

    /**
     * Add support for Tag
     */
    public void handleTag() {
        handle(Type.TAG);
    }

    /**
     * Add support for List<Tag>
     */
    public void handleTagList() {
        handle(Type.TAG_LIST);
    }

    /**
     * Add support for Image
     */
    public void handleImage() {
        handle(Type.IMAGE);
    }
}
