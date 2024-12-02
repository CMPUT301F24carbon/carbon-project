package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The ViewEventsActivity class is an activity that displays a list of events created by an organizer.
 */
public class ViewEventsActivity extends AppCompatActivity {

    private ListView eventsListView;
    private ArrayList<String> eventsList;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
    private String organizerId;
    private ArrayList<String> eventIds;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        db = FirebaseFirestore.getInstance();

        eventsListView = findViewById(R.id.events_list_view);
        eventsList = new ArrayList<>();
        eventIds = new ArrayList<>();

        // Set up the adapter for the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsList);
        eventsListView.setAdapter(adapter);

        organizerId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Disable list view initially
        eventsListView.setEnabled(false);

        // Fetch events from Firestore
        fetchEventsFromDatabase(organizerId);

        eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            if (eventIds.isEmpty()) {
                Log.e("ViewEventsActivity", "Event ID list is empty!");
                return;
            }

            // Get the correct event ID
            String eventId = eventIds.get(position);

            Log.d("OrganizerEventDetails", "Event ID: " + eventId);

            // Navigate to OrganizerEventListDetailsActivity
            Intent intent = new Intent(ViewEventsActivity.this, OrganizerEventListDetailsActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });

        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
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
     * Fetches events from Firestore.
     * @param organizerId
     */
    private void fetchEventsFromDatabase(String organizerId) {
        db.collection("events")
                .whereEqualTo("organizerId", organizerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventId = document.getId(); // Get the event ID
                            String eventName = document.getString("name"); // Get the event name

                            if (eventId != null && eventName != null) {
                                eventIds.add(eventId); // Store event ID
                                eventsList.add(eventName); // Display event name
                            }
                        }

                        if (eventIds.isEmpty() || eventsList.isEmpty()) {
                            Log.e("FetchEvents", "No events found for this organizer.");
                        }

                        adapter.notifyDataSetChanged();

                        // Enable list view once data is loaded
                        eventsListView.setEnabled(true);
                    } else {
                        Log.e("FetchEvents", "Error fetching events", task.getException());
                    }
                });
    }

}
