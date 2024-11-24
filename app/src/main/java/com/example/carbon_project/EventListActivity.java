package com.example.carbon_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * EventListActivity is an activity that displays a list of events using a RecyclerView.
 * It sets up the RecyclerView with an EventAdapter to display the event details.
 */
public class EventListActivity extends AppCompatActivity implements EventAdapter.OnEventClickListener, EditEventFragment.EditEventDialogListener {

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);  // Ensure this layout contains your RecyclerView

        eventRecyclerView = findViewById(R.id.recyclerView_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(events, this);
        eventRecyclerView.setAdapter(eventAdapter);

        // Fetch events from Firestore
        fetchEvents();
    }

    private void fetchEvents() {
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            events.clear();  // Clear any existing events

            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String eventId = document.getId();
                Event event = new Event(eventId);
                event.loadFromFirestore(new Event.DataLoadedCallback() {
                    @Override
                    public void onDataLoaded(HashMap<String, Object> eventData) {
                        // Add the event to the list and notify the adapter about the change
                        runOnUiThread(() -> {
                            events.add(event);
                            eventAdapter.notifyItemInserted(events.size() - 1);  // Notify RecyclerView about new item
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(EventListActivity.this, "Error loading event: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        }).addOnFailureListener(e -> {
            runOnUiThread(() -> {
                Toast.makeText(this, "Failed to retrieve events.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onEventClick(Event event) {
        // Find the position of the clicked event
        int position = events.indexOf(event);
        if (position != -1) {
            EditEventFragment editEventFragment = new EditEventFragment();
            editEventFragment.setEventToEdit(event);
            editEventFragment.setPosition(position);  // Pass the position to the fragment
            editEventFragment.show(getSupportFragmentManager(), "EditEventFragment");
        } else {
            Log.e("EventListActivity", "Event not found in list.");
        }
    }


    @Override
    public void updateEvent(Event event, int position) {
        // Once the event is updated, notify the adapter
        events.set(position, event);  // Replace the event at the specified position
        eventAdapter.notifyItemChanged(position);  // Notify that this specific item has changed

        // Optionally, update the event in Firestore again, if needed
        event.uploadToFirestore();  // Upload the updated event data to Firestore
    }
}
