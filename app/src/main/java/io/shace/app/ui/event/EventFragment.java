package io.shace.app.ui.event;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import io.shace.app.api.models.Image;
import io.shace.app.api.models.Media;
import io.shace.app.tools.IntentTools;
import io.shace.app.tools.MetricTools;
import io.shace.app.tools.NetworkTools;
import io.shace.app.ui.widgets.ObservableScrollView;

/**
 * Created by melvin on 8/28/14.
 */
@EFragment(R.layout.fragment_event)
public class EventFragment extends Fragment implements EventListener, ObservableScrollView.Callbacks {
    private static final String TAG = EventFragment.class.getSimpleName();

    @ViewById(R.id.icon_loader) ProgressBar mLoader;
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
    private String mToken = null;
    private boolean mPswHasFailed = false;

    boolean mFakeActionbarDisplayed = false;

    int mHeight;
    private ViewPropertyAnimator mCurrentAnimation;

    @AfterViews
    protected void init() {
        mActivity = (EventActivity_)getActivity();

        setHasOptionsMenu(true);
        String token = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if (token != null) {
            mToken = token;
            Event.getByToken(this, token);
        } else {
            Log.e(TAG, "Token not provided");
        }
        mScrollView.addCallbacks(this);
    }

    @Override
    public void onEventRetrieved(Event event) {
        mPswHasFailed = false;
        mEvent = event;

        mEventTitle.setText(event.getName());
        mEventDescription.setText(event.getDescription());
        mMainInfo.setBackgroundColor(event.getUsableLightColor());
        mFakeActionbar.setBackgroundColor(event.getUsableLightColor());

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mDetailLayout.getLayoutParams();
        lp.topMargin = mMainInfo.getHeight() + (int)Event.COVER_HEIGHT;
        mDetailLayout.setLayoutParams(lp);

        Image cover = event.getCover();

        if (cover != null) {
            String url = cover.getBigCover();
            NetworkTools.attachImage(url, mCover);
        }
    }

    @Override
    public void onEventRetrievedFailed(ApiError error) {
        Log.e(TAG, "Unhandled Error: " + Integer.toString(error.getCode()));
    }

    @Override
    public void onEventNeedPassword() {
        askPassword(false);
    }

    private void askPassword(boolean hasError) {
        mPswHasFailed = true;

        final EventFragment that = this;
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.password);
        builder.setView(dialogView);

        final EditText passwordView = (EditText)dialogView.findViewById(R.id.password);

        if (hasError) {
            passwordView.setError(getString(R.string.wrong_password));
        }

        builder.setPositiveButton(R.string.unlock, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String password = passwordView.getText().toString();
                Event.access(that, password, mToken);
                dialog.cancel();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                getActivity().finish();
            }
        });

        builder.show();
    }

    @Override
    public void onEventWrongPassword(ApiError error) {
        askPassword(true);
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
        float actionBarHeightDp = MetricTools.pxToDp(actionBarHeight);
        float twenty = MetricTools.dpToPx(20);

        boolean shouldBeDeployed = actionBarHeight + scrollY >= Event.COVER_HEIGHT - twenty;

        boolean switchNeeded = (mFakeActionbarDisplayed == false && shouldBeDeployed)
                || (mFakeActionbarDisplayed && shouldBeDeployed == false);

        if (switchNeeded) {
            mHeight = 0;
            float heightDp = 1f;
            int animDuration = 250;

            if (mCurrentAnimation != null) {
                mCurrentAnimation.cancel();
                mCurrentAnimation.setDuration(0).start();
            }

            if (mFakeActionbarDisplayed == false) {
                float delta = (actionBarHeight + scrollY) - (Event.COVER_HEIGHT);
                float height = actionBarHeight - delta;

                mFakeActionbar.setPivotY(mFakeActionbar.getMeasuredHeight());
                mFakeActionbar.setPivotX(0f);

                mFixedHeader.setPivotY(mFixedHeader.getMeasuredHeight());
                mFixedHeader.setPivotX(0f);

                heightDp = MetricTools.pxToDp(height);
            }

            if (heightDp != 1 && heightDp < actionBarHeightDp) {
                // the swipe was too fast and we don't need any animation
                heightDp = (int)actionBarHeightDp + 20;
                animDuration = 0;
            }

            mCurrentAnimation = mFakeActionbar.animate();
            mCurrentAnimation.scaleY(heightDp)
                    .setInterpolator(new DecelerateInterpolator(2f))
                    .setDuration(animDuration)
                    .start();

            mHeight = (heightDp == 1f) ? (0) : (int)heightDp;
            mFakeActionbarDisplayed = !mFakeActionbarDisplayed;
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
        mLoader.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
    }

    @Override
    public void onPostExecute() {
        mActivity.onPostExecute();
        if (mPswHasFailed == false) {
            mLoader.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
        }
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