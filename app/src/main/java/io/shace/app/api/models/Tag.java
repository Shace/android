package io.shace.app.api.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.shace.app.api.DeserializerBuilder;

/**
 * Created by melvin on 8/14/14.
 */
public class Tag {
    private String name;
    private String slug;
    @SerializedName("media")  private int mediaId;
    @SerializedName("creator")  private int creatorId;
    private long creation;


    /**
     * Getter and Setters
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    /**
     * Json parsing
     */


    public static List<Tag> fromJson(JSONArray response) {
        DeserializerBuilder<List<Tag>> builder = new DeserializerBuilder<List<Tag>>();
        builder.setMainType(DeserializerBuilder.Type.TAG_LIST);
        builder.handleTagList();
        return builder.buildFromJson(response.toString());
    }

    public static Tag fromJson(JSONObject response) {
        DeserializerBuilder<Tag> builder = new DeserializerBuilder<Tag>();
        builder.setMainType(DeserializerBuilder.Type.TAG);
        builder.handleTag();
        return builder.buildFromJson(response.toString());
    }

    /**
     * Task Launchers
     */
}
