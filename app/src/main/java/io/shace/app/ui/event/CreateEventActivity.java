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
import android.widget.Button;
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
    @ViewById(R.id.saveEvent) protected Button mSubmitButton;

    private String[] mPrivacyValues = null;

    public static final String EXTRA_KEY_MODE = "mode";
    public static final String EXTRA_VALUE_MODE_ADD = "add";
    public static final String EXTRA_VALUE_MODE_EDIT = "edit";

    /**
     * Editing mode only
     */

    private boolean editingMode = false;
    private Event mEvent = null;

    @AfterViews
    protected void init() {
        mPrivacyValues = getResources().getStringArray(R.array.privacy_values);
        mPassword.setOnEditorActionListener(this);

        initMode();
        initPrivacy();
    }

    private void initPrivacy() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.privacy_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPrivacy.setAdapter(adapter);
        mPrivacy.setOnItemSelectedListener(this);
    }

    private void initMode() {
        mToken.setFilters(new TokenFilter[]{ new TokenFilter() });
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String mode = bundle.getString(EXTRA_KEY_MODE);
            String token = bundle.getString(Intent.EXTRA_TEXT);

            if (mode != null && mode.equals(EXTRA_VALUE_MODE_EDIT)) {
                initEdition(token);
            } else {
                initCreation(token);
            }
        }
    }

    private void initEdition(String token) {
        if (token != null) {
            setTitle(getString(R.string.title_activity_update_event));
            mSubmitButton.setText(getResources().getText(R.string.update));
            editingMode = true;
            mToken.setVisibility(View.GONE);
            Event.getByToken(this, token);
        } else {
            Log.e(TAG, "Edit mode needs a Token");
        }
    }

    private void initCreation(String token) {
        if (token != null) {
            mToken.setText(token);
            mToken.setEnabled(false);
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        return customUpNavigation();
    }


    @Click
    protected void saveEvent() {
        int privacy = mPrivacy.getSelectedItemPosition();
        String name = mName.getText().toString();
        String description = mDescription.getText().toString();

        if (mEvent == null) {
            mEvent = new Event();
        }

        if (editingMode == false) {
            mEvent.setToken(mToken.getText().toString());
        }

        mEvent.setPrivacy(mPrivacyValues[privacy]);
        mEvent.setPassword(mPassword.getText().toString());
        mEvent.setDescription(description);
        mEvent.setName(name);
        mEvent.save(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
        if (actionID == EditorInfo.IME_ACTION_DONE) {
            saveEvent();
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
        IntentTools.newReplacingIntentWithExtraString(this, EventActivity_.class, Intent.EXTRA_TEXT, event.getToken());
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
    public void onEventUpdated(Event event) {
        finish();
    }

    @Override
    public void onEventUpdatedFail(ApiError error) {
        if (error.is(ApiError.PARAMETERS_ERROR)) {
            HashMap<String, TextView> fields = new HashMap<String, TextView>();
            fields.put("name", mName);
            fields.put("description", mDescription);
            fields.put("password", mPassword);

            checkFormError(error, fields);
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

    /**
     * Called for an edition
     */
    @Override
    public void onEventRetrieved(Event event) {
        mEvent = event;
        mName.setText(mEvent.getName());
        mDescription.setText(mEvent.getDescription());
        mPassword.setText(mEvent.getPassword());
        mPrivacy.setSelection(getPrivacyKey(mEvent.getPrivacy()));
    }

    private int getPrivacyKey(String privacy) {
        int length = mPrivacyValues.length;

        for (int i = 0; i < length; i++) {
            if (mPrivacyValues[i].equals(privacy)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Called for an edition
     */
    @Override
    public void onEventRetrievedFailed(ApiError error) {
        Log.e(TAG, "The provided event does not exists or User not allowed");
        finish();
    }

    @Override
    public void onEventNeedPassword() {}

    @Override
    public void onEventWrongPassword(ApiError error) {

    }

    @Override
    public void onEventsFound(List<Event> events) {}
}
