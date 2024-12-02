package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.remote.EspressoRemoteMessage;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Event;
import com.example.carbon_project.R;
import com.example.carbon_project.View.EventsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntrantEventsListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView eventsListView;
    private EventsAdapter adapter;
    private ArrayList<String> eventsList;
    private Entrant entrant;
    private ArrayList<String> eventIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_events);

        String entrantId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ListView and adapter
        eventsListView = findViewById(R.id.eventsListView);
        eventsList = new ArrayList<>();
        eventIds = new ArrayList<>();
        adapter = new EventsAdapter(this, eventsList);
        eventsListView.setAdapter(adapter);

        // Set up the onItemClickListener for the ListView
        eventsListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String eventId = eventIds.get(position);
            Intent intent = new Intent(EntrantEventsListActivity.this, EventDetailsActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });

        // Fetch entrant data from Firestore
        db.collection("users").document(entrantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> userData = documentSnapshot.getData();
                                entrant = new Entrant(userData);

                                // Fetch all events and update the ListView
                                for (String eventId : entrant.getJoinedEvents()) {
                                    System.out.println(eventId);
                                    addEventToList(eventId);
                                }
                            }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Helper method to fetch event data from Firestore
    private void addEventToList(String eventId) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        Event event = new Event(eventData);

                        String status = event.getStatus(entrant.getUserId());

                        String eventText = event.getName() + "          Status: " + status + "\n"
                                + event.getDescription() + "\n"
                                + event.getStartDate() + " to " + event.getEndDate() + "\n"
                                + "Capacity: " + event.getCapacity();

                        // Add the event info to the list and the eventId to the ids list
                        eventsList.add(eventText);
                        eventIds.add(eventId);

                        // Notify the adapter to update the ListView
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(EntrantEventsListActivity.this, "Error loading events: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
