package io.shace.app.ui.event;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.List;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.tools.IntentTools;
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

    boolean mAnimDone = false;

    int mHeight;

    @AfterViews
    protected void init() {
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
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        int actionBarHeight = 0;

        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }


        int scrollY = mScrollView.getScrollY();





        // todo put into BaseActivity
        Resources r = getResources();
        float twenty = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

        // todo Fix fast scroll
        if (actionBarHeight + scrollY >= Event.COVER_HEIGHT - twenty) {
            if (mAnimDone == false) {
                float delta = (actionBarHeight + scrollY) - (Event.COVER_HEIGHT);
                float height = actionBarHeight - delta;

                mFakeActionbar.setPivotY(mFakeActionbar.getMeasuredHeight());
                mFakeActionbar.setPivotX(0f);

                mFixedHeader.setPivotY(mFixedHeader.getMeasuredHeight());
                mFixedHeader.setPivotX(0f);

                // move to utilities class
                float scale = getResources().getDisplayMetrics().density;
                float heightDp = height / scale + 0.5f;

                mHeight = (int)heightDp;
                mFakeActionbar.animate()
                        .scaleY(heightDp)
                        .setInterpolator(new DecelerateInterpolator(2f))
                        .setDuration(250)
                        .start();

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
                mAnimDone = !mAnimDone;
            }
        }





        // Sticky header
        int[] viewLocation = new int[2];
        mMainInfo.getLocationOnScreen(viewLocation);

        int padding = 0;

        if (mHeight > 0) {
            padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHeight, r.getDisplayMetrics());
            if (padding > 0) {
                // todo not 40
                padding -= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
            }
        } else {
            // todo try to find good values
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

        // Parallax effect
        mCover.setTranslationY(scrollY * 0.5f);
    }


    @Override
    public void onEventsFound(List<Event> events) {}

    @Override
    public void onEventCreated(Event event) {}

    @Override
    public void onEventCreatedFail(ApiError error) {}

    @Override
    public void onPreExecute() {}

    @Override
    public void onPostExecute() {}
}