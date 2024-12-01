package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrganizerEventListDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView eventName, eventDescription, eventCapacity, eventStart, eventEnd;
    private ImageView eventImage;
    private Button viewMapButton, viewWaitingListButton, viewSelectedListButton, viewCancelledListButton, viewEnrolledListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind views
        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        eventCapacity = findViewById(R.id.event_capacity);
        eventStart = findViewById(R.id.event_start_date);
        eventEnd = findViewById(R.id.event_end_date);
        eventImage = findViewById(R.id.event_image);

        viewMapButton = findViewById(R.id.view_map_button);
        viewWaitingListButton = findViewById(R.id.view_waiting_list_button);
        viewSelectedListButton = findViewById(R.id.view_selected_list_button);
        viewCancelledListButton = findViewById(R.id.view_cancelled_list_button);
        viewEnrolledListButton = findViewById(R.id.view_enrolled_list_button);

        // Get the event ID from Intent
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            fetchEventDetails(eventId);
        } else {
            finish(); // Close activity if eventId is missing
        }

        // Button Click Listeners
        viewMapButton.setOnClickListener(v -> openMapActivity(eventId));
        viewWaitingListButton.setOnClickListener(v -> openListActivity(eventId, "waitingList"));
        viewSelectedListButton.setOnClickListener(v -> openListActivity(eventId, "selectedList"));
        viewCancelledListButton.setOnClickListener(v -> openListActivity(eventId, "cancelledList"));
        viewEnrolledListButton.setOnClickListener(v -> openListActivity(eventId, "enrolledList"));

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

    private void fetchEventDetails(String eventId) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve event details
                        String name = documentSnapshot.getString("name");
                        String description = documentSnapshot.getString("description");
                        String startDate = documentSnapshot.getString("startDate");
                        String endDate = documentSnapshot.getString("endDate");
                        Long capacity = documentSnapshot.getLong("capacity");
                        String posterUrl = documentSnapshot.getString("eventPosterUrl");
                        Boolean geolocationRequired = documentSnapshot.getBoolean("geolocationRequired");

                        // Populate views
                        eventName.setText(name);
                        eventDescription.setText(description);
                        eventCapacity.setText("Capacity: " + capacity);
                        eventStart.setText("Start: " + startDate);
                        eventEnd.setText("End: " + endDate);

                        // Handle geolocationRequired field
                        Button viewMapButton = findViewById(R.id.view_map_button);
                        if (geolocationRequired != null && geolocationRequired) {
                            viewMapButton.setVisibility(View.VISIBLE);
                        } else {
                            viewMapButton.setVisibility(View.GONE);
                        }

                        // Handle waiting, selected, cancelled, and enrolled lists
                        // These fields are retrieved from Firestore
                        ArrayList<String> waitingList = (ArrayList<String>) documentSnapshot.get("waitingList");
                        ArrayList<String> selectedList = (ArrayList<String>) documentSnapshot.get("selectedList");
                        ArrayList<String> cancelledList = (ArrayList<String>) documentSnapshot.get("cancelledList");
                        ArrayList<String> enrolledList = (ArrayList<String>) documentSnapshot.get("enrolledList");

                        // Use the lists as needed
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    private void openMapActivity(String eventId) {
        // Intent to open the map activity, passing the event ID
        Intent intent = new Intent(this, OrganizerMapActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    private void openListActivity(String eventId, String listType) {
        // Intent to open a new list activity, passing the event ID and list type
        Intent intent = new Intent(this, OrganizerUserListActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("listType", listType); // Pass which list to view
        startActivity(intent);
    }
}