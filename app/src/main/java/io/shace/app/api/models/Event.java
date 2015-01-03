package io.shace.app.api.models;


import android.graphics.Color;
import android.util.Log;

import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.App;
import io.shace.app.R;
import io.shace.app.api.serialization.TypeBuilder;
import io.shace.app.api.Model;
import io.shace.app.api.Task;
import io.shace.app.api.cache.models.EventColor;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.tasks.eventTasks.Access;
import io.shace.app.api.tasks.eventTasks.Create;
import io.shace.app.api.tasks.eventTasks.Get;
import io.shace.app.api.tasks.eventTasks.Search;
import io.shace.app.api.tasks.eventTasks.Update;

/**
 * Created by melvin on 8/14/14.
 */
public class Event extends Model {
    private String id = null;
    @Expose private String token;
    @Expose private String name;
    @Expose private String description;
    @Expose private String privacy;
    @Expose private String password;

    private String creation;
    private List<Media> medias = new ArrayList<Media>();
    private Image cover;
    /**
     * in-class vars
     */



    private transient int mColor = 0;
    private static transient final String TAG = Event.class.getSimpleName();
    public static transient final String[] COLORS = App.getContext().getResources().getStringArray(R.array.card_colors);
    public static transient final String[] LIGHT_COLORS = App.getContext().getResources().getStringArray(R.array.card_colors_light);
    public static transient final float COVER_HEIGHT = App.getContext().getResources().getDimension(R.dimen.cover_height);

    /**
     * Getters/Setters
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
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

    public int getUsableColor() {
        if (mColor == 0) {
            EventColor eventColor = getEventColor();
            if (eventColor != null) {
                mColor = eventColor.getColor();
            }
        }

        return Color.parseColor(COLORS[mColor]);
    }

    public int getUsableLightColor() {
        if (mColor == 0) {
            EventColor eventColor = getEventColor();
            if (eventColor != null) {
                mColor = eventColor.getColor();
            }
        }
        return Color.parseColor(LIGHT_COLORS[mColor]);
    }

    public void setColor() {
        mColor = App.getRandom().nextInt(Event.COLORS.length);
        saveColor();
    }

    /**
     * Cache methods
     */

    protected EventColor getEventColor() {
        if (getId() != null) {
            List<EventColor> eventColorList = EventColor.find(EventColor.class, "token = ?", getToken());

            int length = eventColorList.size();
            if (length > 0) {
                return eventColorList.get(0);
            }
        }
        return null;
    }

    private void saveColor() {
        if (getId() != null) {
            EventColor eventColor = getEventColor();

            if (eventColor != null) {
                eventColor.setColor(mColor);
            } else {
                eventColor = new EventColor(getToken(), mColor);
            }
            eventColor.save();
            Log.i(TAG, Long.toString(eventColor.getId()));
        } else {
            Log.i(TAG, "Event id: " + getId());
        }
    }


    /**
     * Json parsing
     */

    public static List<Event> fromJson(JSONArray response) {
        TypeBuilder<List<Event>> builder = new TypeBuilder<List<Event>>();
        builder.setMainType(TypeBuilder.Type.EVENT_LIST);
        builder.handleEventList();
        return builder.buildFromJson(response.toString());
    }

    public static Event fromJson(JSONObject response) {
        TypeBuilder<Event> builder = new TypeBuilder<Event>();
        builder.setMainType(TypeBuilder.Type.EVENT);
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
        if (getId() == null) {
            Task task = new Create(listener);
            task.exec(this);
        } else {
            Map<String, String> data = this.mapData();
            data.put("event_token", getToken());

            Task task = new Update(listener);
            task.exec(data);
        }
    }

    /**
     * Get an event
     *
     * @param listener
     * @param token
     */
    public static void getByToken(EventListener listener, String token) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("event_token", token);

        Task task = new Get(listener);
        task.exec(data);
    }

    /**
     * Access an event
     *
     * @param listener
     */
    public static void access(EventListener listener, String password, String token) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("event_token", token);
        data.put("password", password);

        Task task = new Access(listener);
        task.exec(data);
    }
}
