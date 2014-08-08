package io.shace.app.api.models;

/**
 * Created by melvin on 8/6/14.
 */
abstract public class Model {
    protected int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
