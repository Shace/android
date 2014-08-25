package io.shace.app.ui.navigationDrawer;

import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;

import java.util.ArrayList;

import io.shace.app.App;
import io.shace.app.R;

/**
 * Created by melvin on 8/25/14.
 */
public class NavigationDrawer {
    // < 100 = Main layout
    public static final int ITEM_HOME = 0;
    public static final int ITEM_NEARBY = 10;
    public static final int ITEM_NEW_EVENT = 20;

    // >= 100 = small layout
    public static final int ITEM_SETTINGS = 100;
    public static final int ITEM_SIGN_OUT = 110;
    public static final int ITEM_HELP = 120;
    public static final int ITEM_ABOUT = 130;

    private static final ArrayList<Integer> mItemList = populateItemList();
    private static final SparseIntArray mItemToName = populateItemToName();
    private static final SparseIntArray mItemToIcon = populateItemToIcon();

    private static ArrayList<Integer> populateItemList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(ITEM_HOME);
        list.add(ITEM_NEARBY);
        list.add(ITEM_NEW_EVENT);

        list.add(ITEM_SETTINGS);
        list.add(ITEM_SIGN_OUT);
        list.add(ITEM_HELP);
        list.add(ITEM_ABOUT);
        return list;
    }

    private static SparseIntArray populateItemToName() {
        SparseIntArray data = new SparseIntArray();

        data.put(ITEM_HOME, R.string.nav_home);
        data.put(ITEM_NEARBY, R.string.nav_nearby);
        data.put(ITEM_NEW_EVENT, R.string.nav_new_event);
        data.put(ITEM_SETTINGS, R.string.nav_settings);
        data.put(ITEM_SIGN_OUT, R.string.nav_sign_out);
        data.put(ITEM_HELP, R.string.nav_help);
        data.put(ITEM_ABOUT, R.string.nav_about);

        return data;
    }

    private static SparseIntArray populateItemToIcon() {
        SparseIntArray data = new SparseIntArray();

        data.put(ITEM_HOME, android.R.drawable.ic_menu_myplaces);
        data.put(ITEM_NEARBY, android.R.drawable.ic_menu_mylocation);
        data.put(ITEM_NEW_EVENT, android.R.drawable.ic_menu_add);
        data.put(ITEM_SETTINGS, android.R.drawable.ic_menu_preferences);
        data.put(ITEM_SIGN_OUT, android.R.drawable.ic_menu_revert);
        data.put(ITEM_HELP, android.R.drawable.ic_menu_help);
        data.put(ITEM_ABOUT, android.R.drawable.ic_menu_info_details);

        return data;
    }

    public static ArrayList<Integer> getItems() {
        return mItemList;
    }

    public static String getItemName(int item) {
        Integer id = mItemToName.get(item);
        return App.getCurrentActivity().getString(id);
    }

    public static Drawable getItemIcon(int item) {
        return App.getCurrentActivity().getResources().getDrawable(mItemToIcon.get(item));
    }
}
