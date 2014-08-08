package io.shace.app.api.models;

import com.google.gson.annotations.SerializedName;

import io.shace.app.api.models.listeners.TokenListener;
import io.shace.app.api.models.tasks.Task;
import io.shace.app.api.models.tasks.tokenTasks.Generate;

/**
 * Created by melvin on 8/7/14.
 */
public class Token extends Model{
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
    public static void update(TokenListener listener) {
        // todo Update the guest token using PUT
    }
}
