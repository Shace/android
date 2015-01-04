package io.shace.app.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.shace.app.R;
import io.shace.app.api.ApiError;
import io.shace.app.api.adapters.EventAdapter;
import io.shace.app.api.listeners.EventListener;
import io.shace.app.api.models.Event;
import io.shace.app.tools.IntentTools;
import io.shace.app.ui.event.CreateEventActivity_;
import io.shace.app.ui.event.EventActivity_;
import io.shace.app.ui.widgets.FloatingActionButton;


/**
 * Created by melvin on 7/15/14.
 */
@EFragment(R.layout.fragment_homepage)
public class HomepageFragment extends Fragment implements EventListener,  AdapterView.OnItemClickListener {
    MainActivity_ mActivity;
    FloatingActionButton mCreateEventButton;

    @ViewById(R.id.myevents)
    ListView mListViewEvent;


    @AfterViews
    void init() {
        mActivity = (MainActivity_) getActivity();
        mActivity.setActionBarTitle(R.string.title_homepage);

        Event.list(this);

        initCreateButton();
    }

    //TODO Reuse
    private void initCreateButton() {
        mCreateEventButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.float_action_add))
                .withButtonColor(getResources().getColor(R.color.green))
                .withGravity(Gravity.BOTTOM | Gravity.END)
                .withMargins(0, 0, 16, 16)
                .create();

        mCreateEventButton.showFloatingActionButton();

        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // todo reload the list of event (in case the user just created a new one);
    }

    @Override
    public void onEventsFound(List<Event> events) {
        if (events.size() > 0) {
            EventAdapter adapter = new EventAdapter(getActivity(), R.layout.event_list_item, events);
            mListViewEvent.setAdapter(adapter);
            mListViewEvent.setOnItemClickListener(this);
        }
    }

    @Override
    public void onEventRetrieved(Event event) {

    }

    @Override
    public void onEventRetrievedFailed(ApiError error) {

    }

    @Override
    public void onEventNeedPassword() {

    }

    @Override
    public void onEventWrongPassword(ApiError error) {

    }

    @Override
    public void onEventCreated(Event event) {

    }

    @Override
    public void onEventCreatedFail(ApiError error) {

    }

    @Override
    public void onEventUpdated(Event event) {

    }

    @Override
    public void onEventUpdatedFail(ApiError error) {

    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute() {

    }

    public void createEvent() {
        IntentTools.newBasicIntentWithExtraString(CreateEventActivity_.class, Intent.EXTRA_TEXT, "");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tokenView = (TextView) view.findViewById(R.id.token);
        String token = tokenView.getText().toString();
        IntentTools.newBasicIntentWithExtraString(getActivity(), EventActivity_.class, Intent.EXTRA_TEXT, token);
    }
}
