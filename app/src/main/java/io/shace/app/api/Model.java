package io.shace.app.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

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

    public Map<String,String> mapData() {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(json, stringStringMap);
    }
}
