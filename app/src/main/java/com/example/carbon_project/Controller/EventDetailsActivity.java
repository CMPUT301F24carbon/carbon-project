package com.example.carbon_project.Controller;

import android.content.Intent;
import android.app.AlertDialog;

import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    // UI elements
    private TextView eventName, eventDescription;
    private Button joinButton, leaveButton;

    // Firebase and data objects
    private FirebaseFirestore db;
    private String userId;
    private String eventId;

    List<String> waitingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        joinButton = findViewById(R.id.join_event_button);
        leaveButton = findViewById(R.id.leave_event_button);

        setupBottomNavigation();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
        }
        userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();

        loadEventData();
    }

    // Load event data from Firestore
    private void loadEventData() {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String eventNameString = documentSnapshot.getString("name");
                        String eventDescriptionString = documentSnapshot.getString("description");
                        String eventLocationString = documentSnapshot.getString("location");
                        Boolean isGeolocationRequired = documentSnapshot.getBoolean("geolocationRequired");
                        waitingList = (List<String>) documentSnapshot.get("waitingList");

                        // Setup UI
                        eventName.setText(eventNameString != null ? eventNameString : "Event name not provided");
                        eventDescription.setText(eventDescriptionString != null ? eventDescriptionString : "No description available.");

                        leaveButton.setVisibility(View.GONE);
                        updateJoinLeaveButtonVisibility();

                        joinButton.setOnClickListener(v -> handleJoinEvent(isGeolocationRequired));
                        leaveButton.setOnClickListener(v -> updateEventWaitingList(false));

                    } else {
                        showError("Event details not found");
                    }
                })
                .addOnFailureListener(e -> showError("Error retrieving event details: " + e.getMessage()));
    }

    // Update visibility of join and leave buttons
    private void updateJoinLeaveButtonVisibility() {
        if (waitingList != null && waitingList.contains(userId)) {
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
        } else {
            joinButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
        }
    }

    // Handle join event logic
    private void handleJoinEvent(Boolean isGeolocationRequired) {
        if (Boolean.TRUE.equals(isGeolocationRequired)) {
            promptGeolocationConsent(() -> updateEventWaitingList(true));
        } else {
            updateEventWaitingList(true);
        }
    }

    // Update waiting list in Firestore
    private void updateEventWaitingList(boolean isJoining) {
        String operation = isJoining ? "add" : "remove";
        FieldValue action = isJoining ? FieldValue.arrayUnion(userId) : FieldValue.arrayRemove(userId);

        db.collection("events").document(eventId)
                .update("waitingList", action)
                .addOnSuccessListener(aVoid -> {
                    String message = isJoining ? "Successfully joined the event!" : "Successfully left the event!";
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    updateJoinLeaveButtonVisibility();
                })
                .addOnFailureListener(e -> showError("Failed to " + operation + " the event: " + e.getMessage()));
    }

    // Prompt for geolocation consent
    private void promptGeolocationConsent(Runnable onConsent) {
        new AlertDialog.Builder(this)
                .setTitle("Location Sharing Required")
                .setMessage("This event requires you to share your location. Do you want to proceed?")
                .setPositiveButton("OK", (dialog, which) -> onConsent.run())
                .setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(this, "Location sharing is required to join this event.", Toast.LENGTH_SHORT).show())
                .show();
    }

    // Setup bottom navigation
    private void setupBottomNavigation() {
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

    // Handle back button press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Show error message
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
