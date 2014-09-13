package io.shace.app.api.serialization.models;

import java.util.List;

import io.shace.app.api.models.Tag;
import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/16/14.
 */
public class TagList extends Serialization<List<Tag>> {
    private static final String TAG = TagList.class.getSimpleName();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.TAG_LIST);
        builder.handleEvent();
    }
}
