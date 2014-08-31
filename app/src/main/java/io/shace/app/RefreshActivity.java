package io.shace.app;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by melvin on 8/30/14.
 */
abstract public class RefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
    protected SwipeRefreshLayout mRefreshView = null;

    /**
     * Start the animation
     */
    protected void startRefreshAnimation() {
        mRefreshView.setRefreshing(true);
    }

    /**
     * Stop the animation
     */
    protected void stopRefreshAnimation() {
        mRefreshView.setRefreshing(false);
    }

    /**
     * Enable user gesture
     */
    protected void enableGesture() {
        mRefreshView.setEnabled(true);
    }

    /**
     * Disable user gesture
     */
    protected void disableGesture() {
        mRefreshView.setEnabled(false);
    }


    protected void fixScrollUp(ListView listView) {
        listView.setOnScrollListener(this);
    }

    /**
     * The following are used by the API listeners.
     */

    public void onPreExecute() {
        mRefreshView.setRefreshing(true);
    }

    public void onPostExecute() {
        mRefreshView.setRefreshing(false);
    }

    /**
     *  Creation of the layout
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.refresh_item);
        mRefreshView = (SwipeRefreshLayout)findViewById(R.id.refresh);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, mRefreshView, false);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mRefreshView.addView(view, params);
        setUpRefreshView();
    }

    private void setUpRefreshView() {
        mRefreshView.setOnRefreshListener(this);
        mRefreshView.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    /**
     *  Fix for Scroll Up
     */


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRow = 0;

        if (view != null && view.getChildCount() > 0) {
            topRow = view.getChildAt(0).getTop();
        }

        mRefreshView.setEnabled(topRow >= 0);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int state) {}
}
