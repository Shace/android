package io.shace.app.api.models;


import java.util.HashMap;
import java.util.Map;

import io.shace.app.api.Model;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.tasks.eventTasks.Search;

/**
 * Created by melvin on 8/14/14.
 */
public class Event extends Model {
    private String token;
    private String name;
    private String description;
    private String readingPrivacy;
    private String writingPrivacy;
    private String readingPassword;
    private String writingPassword;
    private String creation;
    // private List<Media> medias;
    // private User owner;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getReadingPrivacy() {
        return readingPrivacy;
    }

    public void setReadingPrivacy(String readingPrivacy) {
        this.readingPrivacy = readingPrivacy;
    }

    public String getWritingPrivacy() {
        return writingPrivacy;
    }

    public void setWritingPrivacy(String writingPrivacy) {
        this.writingPrivacy = writingPrivacy;
    }

    public String getReadingPassword() {
        return readingPassword;
    }

    public void setReadingPassword(String readingPassword) {
        this.readingPassword = readingPassword;
    }

    public String getWritingPassword() {
        return writingPassword;
    }

    public void setWritingPassword(String writingPassword) {
        this.writingPassword = writingPassword;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public static void search(EventListener listener, String query) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("query", query);

        Task task = new Search(listener);
        task.exec(data);
    }
}