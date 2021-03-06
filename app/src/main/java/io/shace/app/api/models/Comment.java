package io.shace.app.api.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/14/14.
 */
public class Comment {
    private long creation;
    private String message;
    @SerializedName("owner") private int ownerId;
    @SerializedName("media") private int mediaId;

    /**
     * Getter and Setters
     */

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }


    /**
     * Json parsing
     */


    public static List<Comment> fromJson(JSONArray response) {
        TypeBuilder<List<Comment>> builder = new TypeBuilder<List<Comment>>();
        builder.setMainType(TypeBuilder.Type.COMMENT_LIST);
        builder.handleCommentList();
        return builder.buildFromJson(response.toString());
    }

    public static Comment fromJson(JSONObject response) {
        TypeBuilder<Comment> builder = new TypeBuilder<Comment>();
        builder.setMainType(TypeBuilder.Type.COMMENT);
        builder.handleComment();
        return builder.buildFromJson(response.toString());
    }

    /**
     * Task Launchers
     */

}
