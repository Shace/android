package io.shace.app.api.models;


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
    //private User owner;
    //private Event event;
    //private List<Tag> tags;
    //private Image image;
    private String original;
    //private List<Comment> comments;

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
}
