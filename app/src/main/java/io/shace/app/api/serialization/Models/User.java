package io.shace.app.api.serialization.models;

import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/15/14.
 */
public class User extends Serialization<io.shace.app.api.models.User> {
    private static final String TAG = User.class.getSimpleName();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.USER);
    }
}
