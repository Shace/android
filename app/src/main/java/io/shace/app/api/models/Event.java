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


    public static void search(EventListener listener, String query) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("query", query);

        Task task = new Search(listener);
        task.exec(data);
    }
}
