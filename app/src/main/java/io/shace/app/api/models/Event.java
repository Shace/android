package io.shace.app.api.models;


import android.util.Log;

import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.Model;
import io.shace.app.api.Task;
import io.shace.app.api.cache.models.EventColor;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.tasks.eventTasks.Create;
import io.shace.app.api.tasks.eventTasks.Search;

/**
 * Created by melvin on 8/14/14.
 */
public class Event extends Model {
    @Expose private String token;
    @Expose private String name;
    @Expose private String description;
    @Expose private String privacy;
    @Expose private String password;

    private String creation;
    private List<Media> medias = new ArrayList<Media>();

    /**
     * in-class vars
     */

    private transient int mColor = 0;
    private static transient final String TAG = Event.class.getSimpleName();

    /**
     * Getters/Setters
     */

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

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public boolean hasColor() {
        return getColor() != 0;
    }

    public int getColor() {
        if (mColor == 0) {
            EventColor eventColor = getEventColor();
            if (eventColor != null) {
                mColor = eventColor.getColor();
            }
        }
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
        saveColor(color);

    }

    /**
     * Cache methods
     */

    protected EventColor getEventColor() {
        if (getId() > 0) {
            List<EventColor> eventColorList = EventColor.find(EventColor.class, "token = ?", getToken());

            int length = eventColorList.size();
            if (length > 0) {
                return eventColorList.get(0);
            }
        }
        return null;
    }

    private void saveColor(int color) {
        if (getId() > 0) {
            EventColor eventColor = getEventColor();

            if (eventColor != null) {
                eventColor.setColor(color);
            } else {
                eventColor = new EventColor(getToken(), color);
            }
            eventColor.save();
            Log.i(TAG, Long.toString(eventColor.getId()));
        } else {
            Log.i(TAG, "Event id: " + Integer.toString(getId()));
        }
    }


    /**
     * Json parsing
     */

    public static List<Event> fromJson(JSONArray response) {
        DeserializerBuilder<List<Event>> builder = new DeserializerBuilder<List<Event>>();
        builder.setMainType(DeserializerBuilder.Type.EVENT_LIST);
        builder.handleEventList();
        return builder.buildFromJson(response.toString());
    }

    public static Event fromJson(JSONObject response) {
        DeserializerBuilder<Event> builder = new DeserializerBuilder<Event>();
        builder.setMainType(DeserializerBuilder.Type.EVENT);
        builder.handleEvent();
        return builder.buildFromJson(response.toString());
    }

    /**
     * Task Launchers
     */

    /**
     * Search for an event
     *
     * @param listener
     * @param query token to lookup
     */
    public static void search(EventListener listener, String query) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("query", query);

        Task task = new Search(listener);
        task.exec(data);
    }

    /**
     * Save or update an event
     *
     * @param listener
     */
    public void save(EventListener listener) {
        if (getId() == -1) {
            Task task = new Create(listener);
            task.exec(this);
        } else {
            // update
        }
    }
}
