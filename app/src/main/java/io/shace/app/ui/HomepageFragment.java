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
        mActivity.setActionBarTitle(R.string.title_homepage);
    }

    @Override
    public void onResume() {
        super.onResume();
        // todo reload the list of event (in case the user just created a new one);
    }
}
