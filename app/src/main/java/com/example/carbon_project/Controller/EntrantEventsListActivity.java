package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.R;
import com.example.carbon_project.View.EventsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The EntrantEventsListActivity class is an activity that displays a list of events for an entrant.
 */
public class EntrantEventsListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView eventsListView;
    private EventsAdapter adapter;
    private ArrayList<String> eventsList;
    private Entrant entrant;
    private ArrayList<String> eventIds;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_events);

        entrant = (Entrant) getIntent().getSerializableExtra("userObject");

        if (entrant == null) {
            Toast.makeText(this, "Entrant data is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ListView and adapter
        eventsListView = findViewById(R.id.eventsListView);
        eventsList = new ArrayList<>();
        eventIds = new ArrayList<>(); // Initialize eventIds
        adapter = new EventsAdapter(this, eventsList);
        eventsListView.setAdapter(adapter);

        // Fetch events from the database
        fetchUserSpecificEvents(entrant.getUserId());

        eventsListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String selectedEvent = eventsList.get(position);
            String eventId = eventIds.get(position);

            Intent intent = new Intent(EntrantEventsListActivity.this, EventDetailsActivity.class);
            intent.putExtra("eventDetails", selectedEvent);
            intent.putExtra("entrantObject", entrant);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });

        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Handles the back button click event.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetch events from the database
     * @param userId
     */
    private void fetchUserSpecificEvents(String userId) {
        eventsList.clear();
        eventIds.clear();

        // Fetch events where the user is in the enrolled list
        db.collection("events")
                .whereArrayContains("enrolled", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            addEventToList(document);
                        }

                        // Fetch events where the user is in the waiting list
                        db.collection("events")
                                .whereArrayContains("waitingList", userId)
                                .get()
                                .addOnCompleteListener(waitingTask -> {
                                    if (waitingTask.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : waitingTask.getResult()) {
                                            addEventToList(document);
                                        }

                                        // Update the adapter
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(EntrantEventsListActivity.this, "Failed to load waiting list events.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(EntrantEventsListActivity.this, "Failed to load enrolled events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Add event to list
     * @param document
     */
    private void addEventToList(QueryDocumentSnapshot document) {
        String eventId = document.getId();
        String eventName = document.getString("name");
        String eventStartDate = document.getString("startDate");
        String eventEndDate = document.getString("endDate");
        String eventDescription = document.getString("description");
        Long eventCapacity = document.getLong("capacity");

        String eventText = eventName + "\n"
                + eventDescription + "\n"
                + eventStartDate + " to " + eventEndDate + "\n"
                + "Capacity: " + eventCapacity;

        // Avoid adding duplicate events
        if (!eventIds.contains(eventId)) {
            eventsList.add(eventText);
            eventIds.add(eventId);
        }
    }
}
