package io.shace.app.ui.event;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.filters.TokenFilter;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.tools.IntentTools;
import io.shace.app.api.adapters.EventAdapter;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements EventListener, SearchView.OnQueryTextListener {
    private static final String TAG = "SearchActivity";
    private String mToken = null;
    SearchView mSearchView;
    private CharSequence mTitle;

    @ViewById(R.id.listview_event) ListView mListViewEvent;
    TextView mCreateEventView;

    @AfterViews
    void init() {
        mTitle = getTitle();

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.event_list_add, null);
        mCreateEventView = (TextView) header.findViewById(R.id.create_event_text);

        EventAdapter adapter = new EventAdapter(this, R.layout.event_list_item, new ArrayList<Event>());
        mListViewEvent.addHeaderView(header);
        mListViewEvent.setAdapter(adapter);
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
            mSearchView = (SearchView) searchItem.getActionView();
            mSearchView.setIconifiedByDefault(false);
            setupSearch(searchItem);
        }

        return super.onCreateOptionsMenu(menu);
    }

    protected void setupSearch(MenuItem searchItem) {
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getString(R.string.action_search_hint));
        setTokenFilter();
        mSearchView.setFocusable(true);
        mSearchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void setTokenFilter() {
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) mSearchView.findViewById(id);
        editText.setFilters(new TokenFilter[]{ new TokenFilter() });
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.length() > 0) {
            mToken = newText;
            Event.search(this, newText);
        } else {
            mListViewEvent.setVisibility(View.GONE);
            mCreateEventView.setVisibility(View.GONE);
            // todo display no result found
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onEventsFound(List<Event> events) {
        displayCreateButton(events);

        HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) mListViewEvent.getAdapter();
        EventAdapter adapter = (EventAdapter) headerViewListAdapter.getWrappedAdapter();
        adapter.clear();
        adapter.addAll(events);
        adapter.notifyDataSetChanged();
        mListViewEvent.setVisibility(View.VISIBLE);
    }

    private void displayCreateButton(List<Event> events) {
        if (events != null && events.size() > 0) {
            Event firstEvent = events.get(0);

            if (firstEvent.getToken().equals(mToken)) {
                mCreateEventView.setVisibility(View.GONE);
            } else {
                mCreateEventView.setVisibility(View.VISIBLE);
            }

        } else if (events != null || mToken.length() > 0) {
            mCreateEventView.setVisibility(View.VISIBLE);
        } else {
            mCreateEventView.setVisibility(View.GONE);
        }
    }

    public void createEvent(View v) {
        if (mToken != null && mToken.length() > 0) {
            IntentTools.newBasicIntentWithExtraString(CreateEventActivity_.class, Intent.EXTRA_TEXT, mToken);
        }
    }

    @Override
    public void onEventFound(Event event) {}

    @Override
    public void onEventCreated(Event event) {}

    @Override
    public void onEventCreatedFail(ApiError error) {}

    @Override
    public void onPreExecute() {}

    @Override
    public void onPostExecute() {}
}
