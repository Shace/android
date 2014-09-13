package io.shace.app.ui.event;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.listeners.MediaListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.tools.ToastTools;
import io.shace.app.ui.widgets.FloatingActionButton;

/**
 * Created by melvin on 8/28/14.
 *
 * TODO check the rights
 */
@EFragment(R.layout.fragment_media)
public class MediaFragment extends Fragment implements EventListener, MediaListener {
    private static final String TAG = MediaFragment.class.getSimpleName();

    private static final int FILE_PICKER = 1;

    @ViewById(R.id.gridview) GridView mGridView;

    private Event mEvent = null;
    FloatingActionButton mCreateEventButton;

    @AfterViews
    protected void init() {
        String token = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if (token != null) {
            Event.getByToken(this, token);
        } else {
            Log.e(TAG, "Token not provided");
        }
    }

    // Todo reuse
    private void initCreateButton() {
        mCreateEventButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.float_action_add))
                .withButtonColor(getResources().getColor(R.color.green))
                .withGravity(Gravity.BOTTOM | Gravity.END)
                .withMargins(0, 0, 16, 16)
                .create();

        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPicture();
            }
        });
    }

    public void addPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= 18) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setType("image/*");
        startActivityForResult(intent, FILE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_PICKER && resultCode == Activity.RESULT_OK) {
            ArrayList<String> pictures = new ArrayList<String>();

            if (Build.VERSION.SDK_INT >= 18 && data.getData() == null) {
                ClipData clipdata = data.getClipData();
                for (int i=0; i<clipdata.getItemCount(); i++) {
                    pictures.add(clipdata.getItemAt(i).getUri().toString());
                }
            } else if (data.getData() != null) {
                pictures.add(data.getData().getPath());
            }
            uploadPictures(pictures);
        }
    }

    private void uploadPictures(ArrayList<String> pictures) {
        List<Media> medias = new ArrayList<Media>();

        for (String elem : pictures) {
            File file = new File(elem);

            Media media = new Media();
            media.setName(file.getName());
            medias.add(media);
        }
        Media.addBulk(this, mEvent.getToken(), medias);

//        AsyncHttpClient client = new AsyncHttpClient();
//        JSONObject params = new JSONObject();
//
//        for (String elem : pictures) {
//            File file = new File(elem);
//
//            try {
//                params.put("medias", file);
//                StringEntity entity = null;
//                try {
//                    entity = new StringEntity(params.toString());
//                } catch (UnsupportedEncodingException e) {
//                    Log.e(TAG, "ERROR 2");
//                }
//                String url = Routes.MEDIAS;
//                url = url.replace(":event_token", mEvent.getToken());
//                url = url.replace(":access_token", Token.get().getToken());
//
//                client.post(getActivity(), url, entity, "application/json", new AsyncHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        ToastTools.use().longToast("Success");
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        try {
//                            ToastTools.use().longToast("failed: " + statusCode + " body: " + new String(responseBody, "UTF-8"));
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            } catch (JSONException e) {
//                Log.e(TAG, "ERROR 1");
//            }
//        }
    }

    @Override
    public void onMediasGenerated(List<Media> medias) {
        ToastTools.use().longToast("Ok, got " + medias.size());
    }

    @Override
    public void onMediasGeneratedFail(ApiError error) {
        ToastTools.use().longToast("Failed: " + error.getCode());
    }

    @Override
    public void onEventsFound(List<Event> events) {

    }

    @Override
    public void onEventRetrieved(Event event) {
        mEvent = event;

        initCreateButton();

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

    @Override
    public void onMediaUpdated(Media media) {

    }

    @Override
    public void onMediaUpdatedFail(ApiError error) {

    }

    @Override
    public void onMediaRetrieved(Media media) {

    }

    @Override
    public void onMediaRetrievedFail(ApiError error) {

    }

    @Override
    public void onMediaDeleted(Media media) {

    }

    @Override
    public void onMediaDeletedFail(ApiError error) {

    }
}