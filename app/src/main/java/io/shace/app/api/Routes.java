package io.shace.app.api;

/**
 * Created by melvin on 5/24/14.
 */
public class Routes {
    public static final String ROOT_URL = "http://api.shace.io";

    public static final String ACCESS_TOKEN = ROOT_URL + "/access_token/:token";
    public static final String USERS = ROOT_URL + "/users/:id";
    public static final String ME = ROOT_URL + "/users/me";
}
