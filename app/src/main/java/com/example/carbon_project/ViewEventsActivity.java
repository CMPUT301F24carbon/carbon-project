package com.example.carbon_project;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewEventsActivity extends AppCompatActivity {

    private ListView eventsListView;
    private ArrayList<String> eventsList;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize the ListView and events list
        eventsListView = findViewById(R.id.events_list_view);
        eventsList = new ArrayList<>();

        // Set up the adapter for the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsList);
        eventsListView.setAdapter(adapter);

        organizerId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        // Fetch events from Firestore
        fetchEventsFromDatabase(organizerId);
    }

    private void fetchEventsFromDatabase(String organizerId) {
        db.collection("events")
                .whereEqualTo("organizerId", organizerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extract event data from the document
                            String eventName = document.getString("name");
                            String eventDescription = document.getString("description");
                            String eventText = eventName + " - " + eventDescription;

                            // Add the event to the list
                            eventsList.add(eventText);

                            // Notify the adapter that data has changed
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(ViewEventsActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
