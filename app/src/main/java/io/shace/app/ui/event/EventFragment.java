package io.shace.app.ui.event;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
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

/**
 * Created by melvin on 8/28/14.
 */
@EFragment(R.layout.fragment_event)
public class EventFragment extends Fragment implements EventListener {
    private static final String TAG = EventFragment.class.getSimpleName();

    @ViewById(R.id.header) LinearLayout mHeader;
    @ViewById(R.id.main_picture) NetworkImageView mCover;
    @ViewById(R.id.title) TextView mEventTitle;

    @AfterViews
    protected void init() {
        String token = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if (token != null) {
            Event.getByToken(this, token);
        } else {
            Log.e(TAG, "Token not provided");
        }
    }

    @Override
    public void onEventRetrieved(Event event) {
        Log.d(TAG, event.getName());

        mEventTitle.setText(event.getName());
        mHeader.setBackgroundColor(event.getColorUsableLightColor());

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