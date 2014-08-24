package io.shace.app.ui.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.shace.app.R;
import io.shace.app.api.filters.TokenFilter;

@EActivity(R.layout.activity_create_event)
public class CreateEventActivity extends Activity {
    @ViewById(R.id.token) protected EditText mToken;

    @AfterViews
    protected void init() {
        mToken.setFilters(new TokenFilter[]{ new TokenFilter() });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            String token = bundle.getString(Intent.EXTRA_TEXT);
            mToken.setText(token);
            mToken.setEnabled(false);
        }
    }
}
