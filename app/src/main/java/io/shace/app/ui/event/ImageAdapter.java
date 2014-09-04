package io.shace.app.ui.event;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import io.shace.app.api.models.Media;
import io.shace.app.tools.NetworkTools;

/**
 * Created by melvin on 9/4/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Media> mMedias;

    public ImageAdapter(Context context, List<Media> medias) {
        mContext = context;
        mMedias = medias;
    }

    public int getCount() {
        return mMedias.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        NetworkImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes

            //todo move to utilities class
            Resources r = mContext.getResources();
            int heightDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85, r.getDisplayMetrics());

            imageView = new NetworkImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(heightDp, heightDp));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (NetworkImageView) convertView;
        }

        Media picture = mMedias.get(position);
        String url = picture.getImage().getMedium();
        NetworkTools.attachImage(url, imageView);
        return imageView;
    }
}