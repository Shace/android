package io.shace.app.ui.event;

import android.app.ActionBar;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.shace.app.R;
import io.shace.app.RefreshActivity;
import io.shace.app.api.ApiError;
import io.shace.app.api.adapters.EventAdapter;
import io.shace.app.api.filters.TokenFilter;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.tools.IntentTools;
import io.shace.app.ui.widgets.FloatingActionButton;

@EActivity(R.layout.activity_search)
public class SearchActivity extends RefreshActivity implements EventListener, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, SearchView.OnCloseListener {
    private static final String TAG = "SearchActivity";
    private String mToken = "";
    SearchView mSearchView;
    private CharSequence mTitle;

    @ViewById(R.id.listview_event) ListView mListViewEvent;
    FloatingActionButton mCreateEventButton;

    @AfterViews
    void init() {
        mTitle = getTitle();

        initCreateButton();

        EventAdapter adapter = new EventAdapter(this, R.layout.event_list_item, new ArrayList<Event>());
        mListViewEvent.setAdapter(adapter);
        mListViewEvent.setOnItemClickListener(this);
        fixScrollUp(mListViewEvent);
    }

    private void initCreateButton() {
        mCreateEventButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_edit))
                .withButtonColor(getResources().getColor(R.color.green))
                .withGravity(Gravity.BOTTOM | Gravity.END)
                .withMargins(0, 0, 16, 16)
                .create();

        mCreateEventButton.hideFloatingActionButton();

        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        restoreActionBar();

        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchItem.setIcon(R.drawable.ic_action_search);
            mSearchView = (SearchView) searchItem.getActionView();
            setupSearch(searchItem);
        }

        return true;
    }

    protected void setupSearch(MenuItem searchItem) {
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnCloseListener(this);

        mSearchView.setIconified(false);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_GO);
        mSearchView.setQueryHint(getString(R.string.action_search_hint));
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setFocusable(true);
        mSearchView.requestFocus();

        setTokenFilter();
    }

    private void setTokenFilter() {
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) mSearchView.findViewById(id);
        editText.setFilters(new TokenFilter[]{new TokenFilter()});
        editText.setHintTextColor(getResources().getColor(R.color.white_50));
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        mToken = newText;

        if (newText.length() > 0) {
            Event.search(this, newText);
        } else {
            stopRefreshAnimation();
            mListViewEvent.setVisibility(View.GONE);
            mCreateEventButton.hideFloatingActionButton();
            // todo display no result found
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        return false;
    }

    @Override
    public void onEventsFound(List<Event> events) {
        displayCreateButton(events);

        EventAdapter adapter = (EventAdapter) mListViewEvent.getAdapter();
        adapter.clear();
        adapter.addAll(events);
        adapter.notifyDataSetChanged();
        mListViewEvent.setVisibility(View.VISIBLE);
    }

    private void displayCreateButton(List<Event> events) {
        if (events != null && events.size() > 0) {
            Event firstEvent = events.get(0);

            if (firstEvent.getToken().equals(mToken)) {
                mCreateEventButton.hideFloatingActionButton();
            } else {
                mCreateEventButton.showFloatingActionButton();
            }

        } else if (events != null || mToken.length() > 0) {
            mCreateEventButton.showFloatingActionButton();
        } else {
            mCreateEventButton.hideFloatingActionButton();
        }
    }


    public void createEvent() {
        if (mToken != null && mToken.length() > 0) {
            IntentTools.newBasicIntentWithExtraString(CreateEventActivity_.class, Intent.EXTRA_TEXT, mToken);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tokenView = (TextView) view.findViewById(R.id.token);
        String token = tokenView.getText().toString();
        IntentTools.newBasicIntentWithExtraString(this, EventActivity_.class, Intent.EXTRA_TEXT, token);
    }

    @Override
    public boolean onClose() {
        finish();
        return false;
    }

    @Override
    public void onRefresh() {
        onQueryTextChange(mToken);
    }

    @Override
    public void onEventCreated(Event event) {}

    @Override
    public void onEventCreatedFail(ApiError error) {}

    @Override
    public void onEventUpdated(Event event) {}

    @Override
    public void onEventUpdatedFail(ApiError error) {}

    @Override
    public void onEventRetrieved(Event event) {}

    @Override
    public void onEventRetrievedFailed(ApiError error) {}

    @Override
    public void onEventNeedPassword() {}

    @Override
    public void onEventWrongPassword(ApiError error) {

    }
}
