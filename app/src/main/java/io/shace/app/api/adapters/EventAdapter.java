package io.shace.app.api.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.shace.app.R;
import io.shace.app.api.models.Event;
import io.shace.app.api.models.Media;

/**
 * Created by melvin on 8/14/14.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private static final String TAG = EventAdapter.class.getSimpleName();

    Context mContext;
    int mLayoutResourceId;

    public EventAdapter(Context context, int layoutResourceId, ArrayList<Event> items) {
        super(context, layoutResourceId, items);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    public static class ViewHolder {
        public final TextView token;
        public final ImageView mainPicture;
        public final TextView title;
        public final TextView description;
        public final LinearLayout card;

        public ViewHolder(View view) {
            card = (LinearLayout) view.findViewById(R.id.card);
            token = (TextView) view.findViewById(R.id.token);
            mainPicture = (ImageView) view.findViewById(R.id.main_picture);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

    // Todo Replace the description by the dates (beginning/end) + display some data on the image
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayoutResourceId, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Event item = getItem(position);

        if (item.hasColor() == false) {
            item.setColor();
        }

        List<Media> medias = item.getMedias();

        if (medias != null && medias.size() > 0) {
            Media firstPicture = medias.get(0);
            String url = firstPicture.getImage().getMedium();

            Picasso picasso = Picasso.with(mContext);
            picasso.setLoggingEnabled(true);
            picasso.load(url).into(viewHolder.mainPicture, new Callback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "PICASSO OK");
                }

                @Override
                public void onError() {
                    Log.i(TAG, "PICASSO KO");
                }
            });
        }

        viewHolder.card.setBackgroundColor(item.getColorUsableColor());
        viewHolder.token.setText(item.getToken());
        viewHolder.title.setText(item.getName());
        viewHolder.description.setText(item.getDescription());

        return convertView;
    }
}
