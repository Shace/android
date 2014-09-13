package io.shace.app.api.serialization.models;

import io.shace.app.api.serialization.Serialization;
import io.shace.app.api.serialization.TypeBuilder;

/**
 * Created by melvin on 8/15/14.
 */
public class Token extends Serialization<io.shace.app.api.models.Token> {
    private static final String TAG = Token.class.getSimpleName();

    @Override
    protected void build() {
        builder.setMainType(TypeBuilder.Type.TOKEN);
    }
}
