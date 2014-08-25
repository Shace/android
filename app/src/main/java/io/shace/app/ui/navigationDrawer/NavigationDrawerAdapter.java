package io.shace.app.ui.navigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import io.shace.app.R;

/**
 * Created by melvin on 8/14/14.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<Integer> {
    private static final String TAG = NavigationDrawerAdapter.class.getSimpleName();

    Context mContext;
    int mLayoutResourceId;
    int mSmallLayoutResourceId;

    public NavigationDrawerAdapter(Context context) {
        super(context, R.layout.navigation_list_item, NavigationDrawer.getItems());

        mContext = context;
        mLayoutResourceId = R.layout.navigation_list_item; // also set in the super
        mSmallLayoutResourceId = R.layout.navigation_list_item_small;
    }

    public static class ViewHolder {
        public final ImageView icon;
        public final TextView title;

        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Integer item = getItem(position);

        int layout = (item < 100) ? (mLayoutResourceId) : (mSmallLayoutResourceId);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.title.setText(NavigationDrawer.getItemName(item));
        viewHolder.icon.setImageDrawable(NavigationDrawer.getItemIcon(item));
//
        return convertView;
    }
}
