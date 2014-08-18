package io.shace.app.api.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import io.shace.app.api.DeserializerBuilder;
import io.shace.app.api.Model;
import io.shace.app.api.Task;
import io.shace.app.api.listeners.UserListener;
import io.shace.app.api.tasks.userTasks.Add;
import io.shace.app.tools.IntentTools;
import io.shace.app.ui.boot.SplashScreenActivity_;

/**
 * Created by melvin on 7/15/14.
 */
public class User extends Model {
    private String email;
    private String password = null;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Date inscriptionDate = null;
    private boolean isAdmin = false;

    public User(String email, String password, String firstName, String lastName, Date birthDate) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    // Getter and setter, the cool stuff are in the bottom.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getInscriptionDate() {
        return inscriptionDate;
    }

    public void setInscriptionDate(Date inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


    // Actions, the cool stuffs

    public static boolean isAuthenticated() {
        return Token.get() != null;
    }

    public static boolean isLogged() {
        try {
            return Token.get().getType().equals(Token.TYPE_USER);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isNotLogged() {
        try {
            return Token.get().getType().equals(Token.TYPE_GUEST);
        } catch (NullPointerException e) {
            return true;
        }

    }

    /**
     * Save or update a user
     *
     * @param listener
     */
    public void save(UserListener listener) {
        if (isNotLogged()) {
            Task task = new Add(listener);
            task.exec(this);
        } else {
            // update
        }
    }

    /**
     * Sign out the update a user
     */
    public static void signOut() {
        Token.remove();
        IntentTools.newFullIntent(SplashScreenActivity_.class);
    }

    /**
     * Retrieve a user from is ID
     * @param id
     *
     *
     */
    public void findById(int id) {

    }

    public static User fromJson(JSONObject response) {
        DeserializerBuilder<User> builder = new DeserializerBuilder<User>();
        builder.setMainType(DeserializerBuilder.Type.USER);
        builder.handleUser();
        return builder.buildFromJson(response.toString());
    }

    public static List<User> fromJson(JSONArray response) {
        DeserializerBuilder<List<User>> builder = new DeserializerBuilder<List<User>>();
        builder.setMainType(DeserializerBuilder.Type.USER_LIST);
        builder.handleUserList();
        return builder.buildFromJson(response.toString());
    }
}
