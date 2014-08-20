package io.shace.app.api.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.shace.app.api.DeserializerBuilder;

/**
 * Created by melvin on 8/14/14.
 */
public class Media {
    private String type;
    private String name;
    private String description;
    private Integer rank;
    private String creation;
    @SerializedName("owner") private int ownerId;
    @SerializedName("event") private String eventToken;
    private List<Tag> tags;
    private Image image;
    private long original;
    private List<Comment> comments;


    /**
     * Getter and Setters
     */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getEventToken() {
        return eventToken;
    }

    public void setEventToken(String eventToken) {
        this.eventToken = eventToken;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public long getOriginal() {
        return original;
    }

    public void setOriginal(long original) {
        this.original = original;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Json parsing
     */


    public static List<Media> fromJson(JSONArray response) {
        DeserializerBuilder<List<Media>> builder = new DeserializerBuilder<List<Media>>();
        builder.setMainType(DeserializerBuilder.Type.MEDIA_LIST);
        builder.handleMediaList();
        return builder.buildFromJson(response.toString());
    }

    public static Media fromJson(JSONObject response) {
        DeserializerBuilder<Media> builder = new DeserializerBuilder<Media>();
        builder.setMainType(DeserializerBuilder.Type.MEDIA);
        builder.handleMedia();
        return builder.buildFromJson(response.toString());
    }

    /**
     * Task Launchers
     */

}
