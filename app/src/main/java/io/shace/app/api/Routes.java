package io.shace.app.api;

/**
 * Created by melvin on 5/24/14.
 *
 * Variable syntax:
 *                  :id => variable "id" is mandatory
 *                  ::id => variable "id" is optional
 *
 * TODO: Put the URL parsing functions here
 */
public class Routes {
    public static final String VARIABLES_REGEX = "/::[A-Za-z_-]+";

    public static final String ROOT_URL = "http://api.shace.io";

    public static final String ACCESS_TOKEN = ROOT_URL + "/access_token/::token";
    public static final String USERS = ROOT_URL + "/users/::id?access_token=:access_token";
    public static final String ME = ROOT_URL + "/users/me?access_token=:access_token";
    public static final String EVENT_ACCESS = ROOT_URL + "/events/:token?access_token=:access_token";
}
