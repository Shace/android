package io.shace.app.api.models;

import io.shace.app.api.Model;

/**
 * Created by melvin on 8/18/14.
 */
public class Image extends Model {
    private long creation;
    private String large;
    private String medium;
    private String small;

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }
}
