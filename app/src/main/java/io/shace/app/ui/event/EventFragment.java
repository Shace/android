package io.shace.app.ui.event;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.tools.IntentTools;
import io.shace.app.tools.MetricTools;
import io.shace.app.tools.NetworkTools;
import io.shace.app.tools.ToastTools;
import io.shace.app.ui.widgets.ObservableScrollView;

/**
 * Created by melvin on 8/28/14.
 */
@EFragment(R.layout.fragment_event)
public class EventFragment extends Fragment implements EventListener, ObservableScrollView.Callbacks {
    private static final String TAG = EventFragment.class.getSimpleName();

    @ViewById(R.id.scroll_view) ObservableScrollView mScrollView;
    @ViewById(R.id.fixed_header) LinearLayout mFixedHeader;
    @ViewById(R.id.fake_actionbar) View mFakeActionbar;
    @ViewById(R.id.main_info) LinearLayout mMainInfo;
    @ViewById(R.id.detail_layout) LinearLayout mDetailLayout;
    @ViewById(R.id.main_picture) NetworkImageView mCover;
    @ViewById(R.id.title) TextView mEventTitle;
    @ViewById(R.id.description) TextView mEventDescription;

    private Event mEvent = null;
    private EventActivity_ mActivity;

    boolean mAnimDone = false;

    int mHeight;

    @AfterViews
    protected void init() {
        mActivity = (EventActivity_)getActivity();

        setHasOptionsMenu(true);
        String token = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if (token != null) {
            Event.getByToken(this, token);
        } else {
            Log.e(TAG, "Token not provided");
        }
        mScrollView.addCallbacks(this);
    }

    @Override
    public void onEventRetrieved(Event event) {
        mEvent = event;

        mEventTitle.setText(event.getName());
        mEventDescription.setText(event.getDescription());
        mMainInfo.setBackgroundColor(event.getColorUsableLightColor());
        mFakeActionbar.setBackgroundColor(event.getColorUsableLightColor());

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mDetailLayout.getLayoutParams();
        lp.topMargin = mMainInfo.getHeight() + (int)Event.COVER_HEIGHT;
        mDetailLayout.setLayoutParams(lp);


        List<Media> medias = event.getMedias();

        if (medias != null && medias.size() > 0) {
            Media firstPicture = medias.get(0);
            String url = firstPicture.getImage().getMedium();
            NetworkTools.attachImage(url, mCover);
        }
    }

    @Override
    public void onEventRetrievedFailed(ApiError error) {
        Log.e(TAG, "Unhandled Error: " + Integer.toString(error.getCode()));
    }

    @Override
    public void onEventNeedPassword() {
        ToastTools.use().longToast("Password needed");
    }

    @Click(R.id.view_photos)
    protected void viewPhotos() {
        IntentTools.newBasicIntentWithExtraString(getActivity(), MediaActivity_.class, Intent.EXTRA_TEXT, mEvent.getToken());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_item) {
            Map<String,String> extras = new HashMap<String, String>();
            extras.put(Intent.EXTRA_TEXT, mEvent.getToken());
            extras.put(CreateEventActivity.EXTRA_KEY_MODE, CreateEventActivity.EXTRA_VALUE_MODE_EDIT);
            IntentTools.newBasicIntentWithExtraStrings(getActivity(), CreateEventActivity_.class, extras);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        if (mActivity.recoveredFromPause()) {
            Event.getByToken(this, mEvent.getToken());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        int scrollY = mScrollView.getScrollY();

        actionBarAnim(scrollY);
        stickyHeader(scrollY);

        // Parallax effect
        mCover.setTranslationY(scrollY * 0.5f);
    }

    private void actionBarAnim(int scrollY) {
        int actionBarHeight = MetricTools.getActionbarSize(getActivity());
        float twenty = MetricTools.dpToPx(20);

        // todo Fix fast scroll
        if (actionBarHeight + scrollY >= Event.COVER_HEIGHT - twenty) {
            if (mAnimDone == false) {
                float delta = (actionBarHeight + scrollY) - (Event.COVER_HEIGHT);
                float height = actionBarHeight - delta;

                mFakeActionbar.setPivotY(mFakeActionbar.getMeasuredHeight());
                mFakeActionbar.setPivotX(0f);

                mFixedHeader.setPivotY(mFixedHeader.getMeasuredHeight());
                mFixedHeader.setPivotX(0f);

                float heightDp = MetricTools.pxToDp(height);
                float actionBarHeightDp = MetricTools.pxToDp(actionBarHeight);

                if (heightDp > actionBarHeightDp) {
                    mFakeActionbar.setScaleY(heightDp);
                    mFakeActionbar.animate()
                            .scaleY(heightDp)
                            .setInterpolator(new DecelerateInterpolator(2f))
                            .setDuration(250)
                            .start();
                } else {
                    // fast scroll
                    heightDp = actionBarHeightDp + 20;
                    mFakeActionbar.setScaleY(heightDp);
                }

                mHeight = (int)heightDp;
                mAnimDone = !mAnimDone;
            }
        } else {
            if (mAnimDone) {
                mHeight = 0;
                mFakeActionbar.animate()
                        .scaleY(1)
                        .setInterpolator(new DecelerateInterpolator(2f))
                        .setDuration(250)
                        .start();
                mFakeActionbar.setScaleY(1);
                mAnimDone = !mAnimDone;
            }
        }
    }

    private void stickyHeader(int scrollY) {
        int[] viewLocation = new int[2];
        mMainInfo.getLocationOnScreen(viewLocation);

        int padding = 0;

        if (mHeight > 0) {
            padding = (int)MetricTools.dpToPx(mHeight);
            if (padding > 0) {
                // todo not 40
                padding -= (int)MetricTools.dpToPx(40);
            }
        } else {
            padding = (int)MetricTools.dpToPx(mHeight);
            if (padding > 0) {
                // todo not 40
                padding -= (int)MetricTools.dpToPx(40);
            }
        }


        int endCover = mCover.getTop() + mCover.getHeight();
        int offsetCover = endCover - scrollY - padding;
        int offset = scrollY - mFixedHeader.getTop() + padding;
        int offset2 = endCover - mFixedHeader.getTop();

        if (offsetCover < 0) {
            mFixedHeader.offsetTopAndBottom(offset);
        } else {
            mFixedHeader.offsetTopAndBottom(offset2);
        }
    }

    @Override
    public void onPreExecute() {
        mActivity.onPreExecute();
    }

    @Override
    public void onPostExecute() {
        mActivity.onPostExecute();
    }

    @Override
    public void onEventsFound(List<Event> events) {}

    @Override
    public void onEventCreated(Event event) {}

    @Override
    public void onEventCreatedFail(ApiError error) {}

    @Override
    public void onEventUpdated(Event event) {}

    @Override
    public void onEventUpdatedFail(ApiError error) {}
}