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
    private String token;
    private boolean autoRenew;
    private String lang;
    private long expiration;
    private long creation;
    private String type;
    @SerializedName("user_id") private int userId;

    // todo move into its own file
    private static final String TOKEN_KEY = "token";

    private enum Type {
        GUEST,
        USER
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static Token get() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        String json = pref.getString(TOKEN_KEY, null);

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
        editor.putString(TOKEN_KEY, gson.toJson(this));
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
        postData.put(TOKEN_KEY, Token.get().getToken());
        Task task = new Update(listener);
        task.exec(postData);
    }

    public void delete() {
        remove();
    }

    public static void remove() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}
