package io.shace.app.ui.event;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import io.shace.app.R;
import io.shace.app.api.models.Media;
import io.shace.app.tools.IntentTools;
import io.shace.app.tools.NetworkTools;

/**
 * Created by melvin on 9/4/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Activity mContext;
    private List<Media> mMedias;

    public ImageAdapter(Activity activity, List<Media> medias) {
        mContext = activity;
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
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int heightDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth / 2, r.getDisplayMetrics());

            imageView = new NetworkImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(heightDp, heightDp));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (NetworkImageView) convertView;
        }

        Media picture = mMedias.get(position);
        String url = picture.getImage().getMedium();
        NetworkTools.attachImage(url, imageView);
        final String bigImage = picture.getImage().getLarge();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = (mContext).getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, ImageFragment.newInstance(bigImage)).addToBackStack(null).commit();
            }
        });
        return imageView;
    }
}