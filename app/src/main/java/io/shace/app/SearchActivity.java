package io.shace.app;

import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.shace.app.api.ApiResponse;
import io.shace.app.api.AsyncApiCall;
import io.shace.app.api.Routes;
import io.shace.app.tools.ToastTools;

@EActivity(R.layout.activity_search)
public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {
    private static final String TAG = "SearchActivity";

    @ViewById(R.id.existingEvent) LinearLayout mExistingEvent;
    @ViewById(R.id.unknownEvent) LinearLayout mUnknownEvent;

    @ViewById(R.id.createEventName) TextView mCreateEventName;
    @ViewById(R.id.joinEventName) TextView mJoinEventName;

    SearchView mSearchView;
    private CharSequence mTitle;

    @AfterViews
    void init() {
        mTitle = getTitle();
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
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getString(R.string.action_search_hint));
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.length() > 0) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("token", newText);

            new AsyncApiCall(getApplicationContext()).get(Routes.EVENT_ACCESS, data,
                    new ApiResponse(new int[]{404,403}) {
                        @Override
                        public void onSuccess(JSONObject response) {
                            mExistingEvent.setVisibility(View.VISIBLE);
                            mUnknownEvent.setVisibility(View.GONE);

                            try {
                                mJoinEventName.setText(response.getString("name"));
                            } catch (JSONException e) {
                                ToastTools.use().longToast(getApplicationContext(), R.string.internal_error);
                            }
                        }

                        @Override
                        public void onError(int code, String response) {
                            mExistingEvent.setVisibility(View.GONE);
                            mUnknownEvent.setVisibility(View.VISIBLE);
                            mCreateEventName.setText(newText);
                        }
                    }
            );
        } else {
            mExistingEvent.setVisibility(View.GONE);
            mUnknownEvent.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
