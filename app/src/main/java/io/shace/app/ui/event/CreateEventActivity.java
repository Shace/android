package io.shace.app.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.filters.TokenFilter;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.tools.IntentTools;

@EActivity(R.layout.activity_create_event)
public class CreateEventActivity extends BaseActivity implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener, EventListener {
    private static final String TAG = CreateEventActivity.class.getSimpleName();

    @ViewById(R.id.token) protected AutoCompleteTextView mToken;
    @ViewById(R.id.privacy) protected Spinner mPrivacy;
    @ViewById(R.id.name) protected EditText mName;
    @ViewById(R.id.description) protected EditText mDescription;
    @ViewById(R.id.password) protected EditText mPassword;

    @ViewById(R.id.icon_loader) protected ProgressBar mLoader;
    @ViewById(R.id.create_form) protected View mForm;

    String[] mPrivacyValues = null;

    @AfterViews
    protected void init() {
        mPrivacyValues = getResources().getStringArray(R.array.privacy_values);
        mPassword.setOnEditorActionListener(this);

        initToken();
        initPrivacy();
    }

    private void initPrivacy() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.privacy_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPrivacy.setAdapter(adapter);
        mPrivacy.setOnItemSelectedListener(this);
    }

    private void initToken() {
        mToken.setFilters(new TokenFilter[]{ new TokenFilter() });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            String token = bundle.getString(Intent.EXTRA_TEXT);
            mToken.setText(token);
            mToken.setEnabled(false);
        }
    }

    @Click
    protected void createEvent() {
        int privacy = mPrivacy.getSelectedItemPosition();
        String name = mName.getText().toString();
        String description = mDescription.getText().toString();

        Event event = new Event();
        event.setToken(mToken.getText().toString());
        event.setPrivacy(mPrivacyValues[privacy]);
        event.setPassword(mPassword.getText().toString());
        event.setDescription(description);
        event.setName(name);
        event.save(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
        if (actionID == EditorInfo.IME_ACTION_DONE) {
            createEvent();
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (mPrivacyValues[pos].equals(getString(R.string.privacy_protected))) {
            mPassword.setVisibility(View.VISIBLE);
        } else {
            mPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e(TAG, "Should not been called");
    }

    @Override
    public void onEventCreated(Event event) {
        IntentTools.newReplacingIntent(this, EventActivity.class);
    }

    @Override
    public void onEventCreatedFail(ApiError error) {
        if (error.is(ApiError.PARAMETERS_ERROR)) {
            HashMap<String, TextView> fields = new HashMap<String, TextView>();
            fields.put("token", mToken);
            fields.put("name", mName);
            fields.put("description", mDescription);
            fields.put("password", mPassword);

            checkFormError(error, fields);

            if (mToken.getError() != null) {
                mToken.setEnabled(true);
            }
        }
    }

    @Override
    public void onPreExecute() {
        mForm.setVisibility(View.GONE);
        mLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute() {
        mForm.setVisibility(View.VISIBLE);
        mLoader.setVisibility(View.GONE);
    }

    @Override
    public void onEventsFound(List<Event> events) {}

    @Override
    public void onEventFound(Event event) {}
}
