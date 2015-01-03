package io.shace.app.ui.event;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.Routes;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.listeners.MediaListener;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;
import io.shace.app.api.models.Token;
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
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[] { takePhotoIntent }
                );

        startActivityForResult(chooserIntent, 1);
        /*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= 18) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setType("image/*");
        startActivityForResult(intent, FILE_PICKER);*/
    }
    Bitmap extra = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_PICKER && resultCode == Activity.RESULT_OK) {
            ArrayList<Uri> pictures = new ArrayList<Uri>();
            extra = null;
            if (data.getExtras() != null) {
                extra = (Bitmap) data.getExtras().get("data");
                pictures.add(null);
            } else if (Build.VERSION.SDK_INT >= 18 && data.getData() == null) {
                ClipData clipdata = data.getClipData();
                for (int i = 0; i < clipdata.getItemCount(); i++) {
                    pictures.add(clipdata.getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                pictures.add(uri);
            }

            uploadPictures(pictures);
        }
    }
    ArrayList<Uri> pictures2 = null;
    private void uploadPictures(ArrayList<Uri> pictures) {
        pictures2 = pictures;
        List<Media> medias = new ArrayList<Media>();

        for (Uri elem : pictures) {
            ContentResolver cr = getActivity().getContentResolver();
            Media media = new Media();
            medias.add(media);
        }
        Media.addBulk(this, mEvent.getToken(), medias);
    }

    class SendPhotoTask extends AsyncTask<HttpPost, Void, Void> {

        OnTaskCompleted otc = null;

        SendPhotoTask(OnTaskCompleted otc) {
            this.otc = otc;
        }

        @Override
        protected Void doInBackground(HttpPost... http) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse resp = null;
            try {
                resp = httpclient.execute(http[0]);
            } catch (IOException e) {
            }
            otc.onTaskCompleted();
            return null;
        }
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }

    @Override
    public void onMediasGenerated(List<Media> medias) {
        try {
            int i = 0;
            for (Media media : medias) {
                try {
                    String url = Routes.MEDIA;
                    url = url.replace(":id", media.getId().toString());
                    url = url.replace(":event_token", mEvent.getToken());
                    url = url.replace(":access_token", Token.get().getToken());
                    HttpPost httppost = new HttpPost(url);
                    MultipartEntity entity = new MultipartEntity();

                    if (true) {
                        ContentResolver cr = getActivity().getContentResolver();
                        try {
                            Bitmap bitmap = null;
                            if (extra != null) {
                                bitmap = extra;
                            } else {
                                InputStream inputStream = cr.openInputStream(pictures2.get(i));
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            }
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] data = bos.toByteArray();
                            entity.addPart("file", new ByteArrayBody(data, "image/jpeg", "picture.jpg"));
                        } catch (FileNotFoundException e) {

                        }
                    }

                    httppost.setEntity(entity);
                    final boolean ok = i == medias.size() - 1;
                    new SendPhotoTask(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted() {
                            if (ok) {
                                getActivity().finish();
                                getActivity().startActivity(getActivity().getIntent());
                            }
                        }
                    }).execute(httppost);
                } catch (Exception e) {

                }
                ++i;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//      AsyncHttpClient client = new AsyncHttpClient();
//
//        for (Media media : medias) {
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
        getActivity().setTitle(event.getName() + " - Photos");
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