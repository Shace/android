package io.shace.app.ui;

import android.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import io.shace.app.R;


/**
 * Created by melvin on 7/15/14.
 */
@EFragment(R.layout.fragment_homepage)
public class HomepageFragment extends Fragment {
    MainActivity_ mActivity;

    @AfterViews
    protected void init() {
        mActivity = (MainActivity_) getActivity();
        mActivity.setActionBarTitle(getString(R.string.title_homepage));
    }
}
