package io.shace.app.api;

/**
 * Created by melvin on 8/7/14.
 */
public interface Listener {
    /**
     * Callback called before the query is made
     */
    public void onPreExecute();

    /**
     * Callback called after the query has been made
     */
    public void onPostExecute();
}
