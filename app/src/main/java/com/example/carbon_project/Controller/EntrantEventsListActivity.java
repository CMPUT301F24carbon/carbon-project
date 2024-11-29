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

        entrant = (Entrant) getIntent().getSerializableExtra("entrantObject");

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
        fetchEventsFromDatabase();

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchEventsFromDatabase() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventId = document.getId();
                            String eventName = document.getString("name");
                            String eventDescription = document.getString("description");
                            String eventText = eventName + " - " + eventDescription;


                            eventsList.add(eventText);
                            eventIds.add(eventId);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(EntrantEventsListActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
