package io.shace.app.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import io.shace.app.BaseActivity;
import io.shace.app.R;
import io.shace.app.api.models.User;
import io.shace.app.tools.IntentTools;
import io.shace.app.tools.ToastTools;
import io.shace.app.ui.event.CreateEventActivity_;
import io.shace.app.ui.event.SearchActivity_;
import io.shace.app.ui.navigationDrawer.NavigationDrawer;
import io.shace.app.ui.navigationDrawer.NavigationDrawerFragment;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;



    public void setActionBarTitle(int resource) {
        mTitle = getString(resource);
        getActionBar().setTitle(mTitle);
    }

    @AfterViews
    void init() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        if (position == NavigationDrawer.ITEM_HOME) {
            fragment = new HomepageFragment_();
        } else if (position == NavigationDrawer.ITEM_NEW_EVENT) {
            IntentTools.newBasicIntent(this, CreateEventActivity_.class);
        } else if (position == NavigationDrawer.ITEM_SIGN_OUT) {
            User.signOut();
        } else {
            ToastTools.use().longToast(R.string.todo);
            Log.e(TAG, "Fragment " + position + " not available");
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            openSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearch() {
        IntentTools.newBasicIntent(SearchActivity_.class);
    }
}
