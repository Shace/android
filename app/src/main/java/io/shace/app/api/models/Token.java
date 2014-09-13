package io.shace.app.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.shace.app.api.serialization.TypeBuilder;
import io.shace.app.api.Model;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.TokenListener;
import io.shace.app.api.tasks.tokenTasks.Generate;
import io.shace.app.api.tasks.tokenTasks.Update;
import io.shace.app.tools.PreferenceTools;

/**
 * Created by melvin on 8/7/14.
 */
public class Token extends Model {
    private String token;
    private boolean autoRenew;
    private long expiration;
    private long creation;
    private String type;
    @SerializedName("user_id") private int userId;

    /**
     * In-class attributes
     */

    private static transient final String TAG = Token.class.getSimpleName();

    public static transient final String TYPE_USER = "user";
    public static transient final String TYPE_GUEST = "guest";


    /**
     * Getter and Setters
     */


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

    /**
     * Json parsing
     */

    public static Token fromJson(JSONObject response) {
        TypeBuilder<Token> builder = new TypeBuilder<Token>();
        builder.setMainType(TypeBuilder.Type.TOKEN);
        builder.handleToken();
        return builder.buildFromJson(response.toString());
    }

    public static Token fromJson(String response) {
        TypeBuilder<Token> builder = new TypeBuilder<Token>();
        builder.setMainType(TypeBuilder.Type.TOKEN);
        builder.handleToken();
        return builder.buildFromJson(response);
    }


    /**
     * Extra methods
     */

    public void save() {
        Gson gson = new Gson();
        PreferenceTools.putKey(PreferenceTools.KEY_TOKEN, gson.toJson(this));
    }

    public void delete() {
        remove();
    }

    public static void remove() {
        PreferenceTools.removeKey(PreferenceTools.KEY_TOKEN);
    }


    /**
     * Task Launchers
     */

    public static Token get() {
        String json = PreferenceTools.getKey(PreferenceTools.KEY_TOKEN, null);

        if (json == null) {
            return null;
        }

        return Token.fromJson(json);
    }

    /**
     * Generate a new guest token
     *
     * @param listener instance of TokenListener
     */
    public static void generate(TokenListener listener) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("auto_renew", "true");

        Task task = new Generate(listener);
        task.exec(data);
    }

    /**
     * Update a token
     *
     * @param listener instance of TokenListener
     */
    public static void update(TokenListener listener, Map<String, String> postData) {
        // todo set the string "token" into Route
        postData.put("token", Token.get().getToken());
        Task task = new Update(listener);
        task.exec(postData);
    }
}
