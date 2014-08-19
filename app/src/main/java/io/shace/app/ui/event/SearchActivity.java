package io.shace.app.ui.event;

import android.app.ActionBar;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.ui.EventAdapter;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements EventListener, SearchView.OnQueryTextListener {
    private static final String TAG = "SearchActivity";

    @ViewById(R.id.listview_event) ListView mListViewEvent;

    SearchView mSearchView;
    private CharSequence mTitle;

    @AfterViews
    void init() {
        mTitle = getTitle();

        EventAdapter adapter = new EventAdapter(this, R.layout.event_list_item, new ArrayList<Event>());
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

        mSearchView.setFocusable(true);
        mSearchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.length() > 0) {
            if (newText.matches("[a-zA-Z0-9|-]+")) {
                Event.search(this, newText);
            } else {
                // todo display invalid token
            }
        } else {
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
        EventAdapter adapter = (EventAdapter) mListViewEvent.getAdapter();
        adapter.clear();
        adapter.addAll(events);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEventFound(Event event) {}

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute() {

    }
}
