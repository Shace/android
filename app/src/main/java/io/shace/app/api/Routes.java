package io.shace.app.api;

import io.shace.app.BuildConfig;
import io.shace.app.tools.AssetTools;

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

    public static final String ROOT_URL = (BuildConfig.DEBUG) ? AssetTools.getDevApiUrl() : "http://api.shace.io";

    public static final String ACCESS_TOKEN = ROOT_URL + "/access_token/::token";
    public static final String USERS = ROOT_URL + "/users/::id?access_token=:access_token";
    public static final String ME = ROOT_URL + "/users/me?access_token=:access_token";

    public static final String EVENTS = ROOT_URL + "/events/::event_token?access_token=:access_token";
    public static final String EVENTS_ACCESS = ROOT_URL + "/events/:event_token/access?access_token=:access_token";
    public static final String EVENT_SEARCH = ROOT_URL + "/events/search?q=:query&access_token=:access_token";
}
