package io.shace.app.api.serialization.models;

import java.util.List;

import io.shace.app.api.models.Comment;
import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/16/14.
 */
public class CommentList extends Serialization<List<Comment>> {
    private static final String TAG = CommentList.class.getSimpleName();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.COMMENT_LIST);
        builder.handleComment();
    }
}
