package io.shace.app.ui.event;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.tools.ToastTools;

/**
 * Created by melvin on 8/28/14.
 */
@EFragment(R.layout.fragment_media)
public class MediaFragment extends Fragment implements EventListener {
    private static final String TAG = MediaFragment.class.getSimpleName();

    @ViewById(R.id.gridview) GridView mGridView;


    private Event mEvent = null;

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
    public void onEventsFound(List<Event> events) {

    }

    @Override
    public void onEventRetrieved(Event event) {
        mEvent = event;

        List<Media> medias = event.getMedias();

        if (medias != null) {
            mGridView.setAdapter(new ImageAdapter(getActivity(), medias));
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

    @Override
    public void onEventWrongPassword(ApiError error) {

    }

    @Override
    public void onEventCreated(Event event) {}

    @Override
    public void onEventCreatedFail(ApiError error) {}

    @Override
    public void onEventUpdated(Event event) {}

    @Override
    public void onEventUpdatedFail(ApiError error) {}

    @Override
    public void onPreExecute() {}

    @Override
    public void onPostExecute() {}
}