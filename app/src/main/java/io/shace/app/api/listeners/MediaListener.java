package io.shace.app.api.listeners;

import java.util.List;

import io.shace.app.api.ApiError;
import io.shace.app.api.Listener;
import io.shace.app.api.models.Media;

/**
 * Created by melvin on 8/14/14.
 */
public interface MediaListener extends Listener {
    public void onMediasGenerated(List<Media> medias);
    public void onMediasGeneratedFail(ApiError error);

    public void onMediaUpdated(Media media);
    public void onMediaUpdatedFail(ApiError error);

    public void onMediaRetrieved(Media media);
    public void onMediaRetrievedFail(ApiError error);

    public void onMediaDeleted(Media media);
    public void onMediaDeletedFail(ApiError error);
}
