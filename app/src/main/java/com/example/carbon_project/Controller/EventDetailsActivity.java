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

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Event;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView eventName, eventDescription;
    private Button joinButton, leaveButton;
    private String eventId;
    private FirebaseFirestore db;
    private Entrant entrant;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize UI elements
        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        joinButton = findViewById(R.id.join_event_button);
        leaveButton = findViewById(R.id.leave_event_button);

        // Retrieve event details from Intent
        eventId = getIntent().getStringExtra("eventId");

        db = FirebaseFirestore.getInstance();
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        db.collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        entrant = new Entrant(deviceId, documentSnapshot.getString("name"), documentSnapshot.getString("email"), documentSnapshot.getString("phoneNumber"));
                        db.collection("events").document(eventId).get()
                                .addOnSuccessListener(eventDocumentSnapshot -> {
                                    if (eventDocumentSnapshot.exists()) {
                                        event = new Event(
                                                eventId,
                                                eventDocumentSnapshot.getString("name"),
                                                eventDocumentSnapshot.getString("description"),
                                                eventDocumentSnapshot.getString("organizerId"),
                                                eventDocumentSnapshot.getLong("capacity").intValue(),
                                                eventDocumentSnapshot.get("waitingList", List.class),
                                                eventDocumentSnapshot.get("selectedList", List.class),
                                                eventDocumentSnapshot.get("canceledList", List.class),
                                                eventDocumentSnapshot.get("enrolledList", List.class),
                                                eventDocumentSnapshot.getBoolean("geolocationRequired"),
                                                eventDocumentSnapshot.getString("startDate"),
                                                eventDocumentSnapshot.getString("endDate"),
                                                eventDocumentSnapshot.getString("eventPosterUrl"),
                                                eventDocumentSnapshot.getString("qrCodeUrl")
                                                );
                                        showUI();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error retrieving event details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error retrieving user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void showUI() {
        if (event.getName() != null) {
            eventName.setText(event.getName());
        } else {
            eventName.setText("Event name not provided");
        }
        if (event.getDescription() != null) {
            eventDescription.setText(event.getDescription());
        } else {
            eventDescription.setText("No description available.");
        }

        leaveButton.setVisibility(View.GONE);

        checkIfUserIsInWaitingList();

        // Set onClick listeners for the buttons
        joinButton.setOnClickListener(v -> checkGeolocationRequirement());
        leaveButton.setOnClickListener(v -> leaveEvent());

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

    private void checkIfUserIsInWaitingList() {
        if (eventId == null || entrant.getUserId() == null) {
            Toast.makeText(this, "Failed to load waiting list. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> waitingList = event.getWaitingList();

        if (waitingList != null && waitingList.contains(entrant.getUserId())) {
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
        } else {
            joinButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
        }
    }

    private void checkGeolocationRequirement() {
        Boolean geolocationRequired = event.isGeolocationRequired();

        if (Boolean.TRUE.equals(geolocationRequired)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location Sharing Required")
                    .setMessage("This event requires you to share your location. Do you want to proceed?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        joinEvent();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Toast.makeText(this, "You need to share your location to join this event.", Toast.LENGTH_SHORT).show();
                    })
                    .show();

            // TODO: Implement location handling logic here if necessary
        } else {
            joinEvent();
        }
    }


    private void joinEvent() {
        if (eventId == null || entrant.getUserId() == null) {
            Toast.makeText(this, "Failed to join the event. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("events").document(eventId)
                .update("waitingList", FieldValue.arrayUnion(entrant.getUserId()))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Successfully added to the event!", Toast.LENGTH_SHORT).show();
                    joinButton.setVisibility(View.GONE);
                    leaveButton.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to join the event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void leaveEvent() {
        if (eventId == null || entrant.getUserId() == null) {
            Toast.makeText(this, "Failed to leave the event. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("events").document(eventId)
                .update("waitingList", FieldValue.arrayRemove(entrant.getUserId()))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Successfully left the event!", Toast.LENGTH_SHORT).show();
                    joinButton.setVisibility(View.VISIBLE);
                    leaveButton.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to leave the event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}