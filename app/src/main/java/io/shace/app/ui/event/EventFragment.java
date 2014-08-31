package io.shace.app.ui.event;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.tools.NetworkTools;
import io.shace.app.ui.widgets.ObservableScrollView;

/**
 * Created by melvin on 8/28/14.
 */
@EFragment(R.layout.fragment_event)
public class EventFragment extends Fragment implements EventListener, ObservableScrollView.Callbacks {
    private static final String TAG = EventFragment.class.getSimpleName();

    @ViewById(R.id.fake_actionbar) View mFakeActionbar;
    @ViewById(R.id.scroll_view) ObservableScrollView mScrollView;
    @ViewById(R.id.main_info) LinearLayout mMainInfo;
    @ViewById(R.id.main_picture) NetworkImageView mCover;
    @ViewById(R.id.title) TextView mEventTitle;
    @ViewById(R.id.description) TextView mEventDescription;

    boolean mAnimDone = false;

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
        Log.d(TAG, event.getName());

        mEventTitle.setText(event.getName());
        mEventDescription.setText(event.getDescription());
        mMainInfo.setBackgroundColor(event.getColorUsableLightColor());
        //mFakeActionbar.setBackgroundColor(event.getColorUsableLightColor());

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

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.event, menu);
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        int actionBarHeight = 0;

        Resources r = getResources();
        // TODO set 200 into a const
        float mPictureHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());

        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        int scrollY = mScrollView.getScrollY();
        int mainInfoY = mMainInfo.getScrollY();

        //ViewGroup.LayoutParams lp = mMainInfo.getLayoutParams();

//        if (lp.height != mHeaderHeightPixels) {
//            lp.height = mHeaderHeightPixels;
//            mHeaderBackgroundBox.setLayoutParams(lp);
//        }

        // todo put into BaseActivity
        float twenty = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

//        Log.d(TAG, "Scroll: " + (actionBarHeight + scrollY));
//        Log.d(TAG, "PH: " + (mPictureHeight - twenty));
//        Log.d(TAG, "ab: " + actionBarHeight);

        Log.d(TAG, Integer.toString(mFakeActionbar.getLayoutParams().height));

        if (actionBarHeight + scrollY >= mPictureHeight - twenty) {
            if (mAnimDone == false) {
                float delta = (actionBarHeight + scrollY) - (mPictureHeight - twenty);
                float height = actionBarHeight - delta;

//                Log.d(TAG, "Delta " + delta);
//                Log.d(TAG, "height " + height);

                // todo put in BaseActivity
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float logicalDensity = metrics.density;
                int heightDp = (int) Math.ceil(height / logicalDensity);

//                Log.e(TAG, "DP: " + heightDp);

                // todo trie with mMainAction directly
                mFakeActionbar.animate()
                        .scaleY(height)
                        .setInterpolator(new DecelerateInterpolator(2f))
                        .setDuration(250)
                        .start();
                mAnimDone = !mAnimDone;
            }
        } else {
            if (mAnimDone) {
                mFakeActionbar.animate()
                        .scaleY(1)
                        .setInterpolator(new DecelerateInterpolator(2f))
                        .setDuration(250)
                        .start();
                mAnimDone = !mAnimDone;
            }
        }

        //mMainInfo.setTranslationY(newTop);
//        mAddScheduleButton.setTranslationY(newTop + mHeaderHeightPixels
//                - mAddScheduleButtonHeightPixels / 2);

//        mHeaderBackgroundBox.setPivotY(mHeaderHeightPixels);
//        int gapFillDistance = (int) (mHeaderTopClearance * GAP_FILL_DISTANCE_MULTIPLIER);
//        boolean showGapFill = !mHasPhoto || (scrollY > (mPhotoHeightPixels - gapFillDistance));
//        float desiredHeaderScaleY = showGapFill ?
//                ((mHeaderHeightPixels + gapFillDistance + 1) * 1f / mHeaderHeightPixels)
//                : 1f;
//        if (!mHasPhoto) {
//            mHeaderBackgroundBox.setScaleY(desiredHeaderScaleY);
//        } else if (mGapFillShown != showGapFill) {
//            mHeaderBackgroundBox.animate()
//                    .scaleY(desiredHeaderScaleY)
//                    .setInterpolator(new DecelerateInterpolator(2f))
//                    .setDuration(250)
//                    .start();
//        }
//        mGapFillShown = showGapFill;
//
//        LPreviewUtilsBase lpu = activity.getLPreviewUtils();
//
//        mHeaderShadow.setVisibility(lpu.hasLPreviewAPIs() ? View.GONE : View.VISIBLE);
//
//        if (mHeaderTopClearance != 0) {
//            // Fill the gap between status bar and header bar with color
//            float gapFillProgress = Math.min(Math.max(UIUtils.getProgress(scrollY,
//                    mPhotoHeightPixels - mHeaderTopClearance * 2,
//                    mPhotoHeightPixels - mHeaderTopClearance), 0), 1);
//            lpu.setViewElevation(mHeaderBackgroundBox, gapFillProgress * mMaxHeaderElevation);
//            lpu.setViewElevation(mHeaderContentBox, gapFillProgress * mMaxHeaderElevation + 0.1f);
//            lpu.setViewElevation(mAddScheduleButton, gapFillProgress * mMaxHeaderElevation
//                    + mFABElevation);
//            if (!lpu.hasLPreviewAPIs()) {
//                mHeaderShadow.setAlpha(gapFillProgress);
//            }
//        }
//
//        // Move background photo (parallax effect)
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