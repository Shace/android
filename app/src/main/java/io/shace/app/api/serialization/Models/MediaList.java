package io.shace.app.api.serialization.models;

import java.util.List;

import io.shace.app.api.models.Media;
import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/16/14.
 */
public class MediaList extends Serialization<List<Media>> {
    private static final String TAG = MediaList.class.getSimpleName();

    protected void build() {
        builder.setMainType(TypeBuilder.Type.MEDIA_LIST);
        builder.handleMedia();
    }
}

