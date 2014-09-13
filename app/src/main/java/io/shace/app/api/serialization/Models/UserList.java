package io.shace.app.api.serialization.models;

import java.util.List;

import io.shace.app.api.models.User;
import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/16/14.
 */
public class UserList extends Serialization<List<User>> {
    private static final String TAG = UserList.class.getSimpleName();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.USER_LIST);
        builder.handleUser();
    }
}

