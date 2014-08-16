package io.shace.app.api.cache.models;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by melvin on 8/15/14.
 *
 * TODO: Replace token by eventId when we'll have int and if it's a int
 */
public class EventColor extends SugarRecord<EventColor> {
    private String token; // TODO set eventId as unique (sugarORM 1.4)
    private int color;
    private Date lastAccess;


    public EventColor() {
    }

    public EventColor(String token, int color) {
        this.token = token;
        this.color = color;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String eventId) {
        this.token = eventId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    @Override
    public void save() {
        lastAccess = new Date();
        super.save();
    }
}
