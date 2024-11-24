package com.example.carbon_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * EventListActivity is an activity that displays a list of events using a RecyclerView.
 * It sets up the RecyclerView with an EventAdapter to display the event details.
 */
public class EventListActivity extends NavigationMenu implements EventAdapter.OnEventClickListener, EditEventFragment.EditEventDialogListener {

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> events;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize RecyclerView
        eventRecyclerView = findViewById(R.id.recyclerView_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize events list
        events = new ArrayList<>();

        // Set up the EventAdapter and assign it to the RecyclerView
        eventAdapter = new EventAdapter(events, this);
        eventRecyclerView.setAdapter(eventAdapter);

        // Fetch events from Firestore
        fetchEvents();
    }

    /**
     * Fetches events from the EventManager and updates the RecyclerView.
     */
    private void fetchEvents() {
        EventManager.getInstance().fetchEvents((success, fetchedEvents) -> {
            if (success) {
                // Clear the current list and add all fetched events
                events.clear();
                if (fetchedEvents != null) {
                    events.addAll(fetchedEvents);
                }

                // Notify the adapter to refresh the UI
                eventAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to retrieve events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void updateEvent(Event event, int position) {
        // Update the event in the list
        events.set(position, event);
        eventAdapter.notifyItemChanged(position);
    }

    @Override
    public void onEventClick(Event event) {
        EditEventFragment editEventFragment = new EditEventFragment();
        editEventFragment.setEventToEdit(event, events.indexOf(event));
        editEventFragment.show(getSupportFragmentManager(), "EditEventFragment");
    }
}