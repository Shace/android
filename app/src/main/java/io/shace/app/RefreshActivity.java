package io.shace.app;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by melvin on 8/30/14.
 */
abstract public class RefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
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
    public void enableGesture() {
        mRefreshView.setEnabled(true);
    }

    /**
     * Disable user gesture
     */
    public void disableGesture() {
        mRefreshView.setEnabled(false);
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
}
