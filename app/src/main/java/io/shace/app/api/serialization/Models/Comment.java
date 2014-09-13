package io.shace.app.api.serialization.models;

import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/15/14.
 */
public class Comment extends Serialization<io.shace.app.api.models.Comment> {
    private TypeBuilder<io.shace.app.api.models.Comment> builder = new TypeBuilder<io.shace.app.api.models.Comment>();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.COMMENT);
    }
}
