package io.shace.app.api.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.api.Task;
import io.shace.app.api.listeners.MediaListener;
import io.shace.app.api.serialization.TypeBuilder;
import io.shace.app.api.tasks.mediaTasks.AddBulk;

/**
 * Created by melvin on 8/14/14.
 */
public class Media {
    private String type;
    @Expose private String name;
    @Expose private String description;
    private Integer rank;
    private String creation;
    @SerializedName("owner") private int ownerId;
    @SerializedName("event") private String eventToken;
    private List<Tag> tags;
    private Image image;
    private long original;
    private List<Comment> comments;
    private Integer id;

    public File file;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Json parsing
     */


    public static List<Media> fromJson(JSONArray response) {
        TypeBuilder<List<Media>> builder = new TypeBuilder<List<Media>>();
        builder.setMainType(TypeBuilder.Type.MEDIA_LIST);
        builder.handleMediaList();
        return builder.buildFromJson(response.toString());
    }

    public static Media fromJson(JSONObject response) {
        TypeBuilder<Media> builder = new TypeBuilder<Media>();
        builder.setMainType(TypeBuilder.Type.MEDIA);
        builder.handleMedia();
        return builder.buildFromJson(response.toString());
    }

    /**
     * Task Launchers
     */

    /**
     * Add a list of media
     *
     * @param listener
     * @param token token of the event
     * @param medias media to add
     */
    public static void addBulk(MediaListener listener, String token, List<Media> medias) {
        TypeBuilder<List<Media>> serializer = new TypeBuilder<List<Media>>(TypeBuilder.Type.MEDIA_LIST);
        JsonElement jsonMedias = serializer.toJson(medias);

        JsonObject jsonData = new JsonObject();
        jsonData.add("medias", jsonMedias);

        Map<String, String> urlData = new HashMap<String, String>();
        urlData.put("event_token", token);

        Task task = new AddBulk(listener);
        task.exec(urlData, jsonData);
    }
}
