package io.shace.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.shace.app.R;
import io.shace.app.api.models.Event;

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

        public ViewHolder(View view) {
            token = (TextView) view.findViewById(R.id.token);
            mainPicture = (ImageView) view.findViewById(R.id.main_picture);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

    // Todo set background color
    // Todo set main image
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

        viewHolder.token.setText(item.getToken());
        viewHolder.title.setText(item.getName());
        viewHolder.description.setText(item.getDescription());

        return convertView;
    }
}
