package io.shace.app.api.models;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.shace.app.App;
import io.shace.app.api.models.listeners.TokenListener;
import io.shace.app.api.models.tasks.Task;
import io.shace.app.api.models.tasks.tokenTasks.Generate;
import io.shace.app.api.models.tasks.tokenTasks.Update;

/**
 * Created by melvin on 8/7/14.
 */
public class Token extends Model {
    private enum Type {
        GUEST,
        USER
    }

    private String token;
    private boolean autoRenew;
    private long expiration;
    private long creation;
    private Type type;
    @SerializedName("user_id") private int userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static Token get() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        String json = pref.getString("token", null);

        if (json == null) {
            return null;
        }

        // TODO move into a utility file
        Gson gson = new Gson();
        return gson.fromJson(json, Token.class);
    }

    public void save() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        editor.putString("token", gson.toJson(this));
        editor.apply();
    }

    /**
     * Generate a new guest token
     *
     * @param listener instance of TokenListener
     */
    public static void generate(TokenListener listener) {
        Task task = new Generate(listener);
        task.exec();
    }

    /**
     * Update a token
     *
     * @param listener instance of TokenListener
     */
    public static void update(TokenListener listener, Map<String, String> postData) {
        postData.put("token", Token.get().getToken());
        Task task = new Update(listener);
        task.exec(postData);
    }
}
