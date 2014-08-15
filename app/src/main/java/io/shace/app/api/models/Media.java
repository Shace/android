package io.shace.app.api.models;


import android.media.Image;

import java.util.List;

/**
 * Created by melvin on 8/14/14.
 */
public class Media {
    private String type;
    private String name;
    private String description;
    private Integer rank;
    private String creation;
    private User owner;
    private Event event;
    private List<Tag> tags;
    private Image image;
    private String original;
    private List<Comment> comments;

}
