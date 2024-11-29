package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
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
        String eventDetails = getIntent().getStringExtra("eventDetails");
        entrant = (Entrant) getIntent().getSerializableExtra("entrantObject");
        eventId = getIntent().getStringExtra("eventId");

        if (eventDetails != null && eventDetails.contains(" - ")) {
            String[] details = eventDetails.split(" - ");
            if (details.length > 0) {
                eventName.setText(details[0]);
            }
            if (details.length > 1) {
                eventDescription.setText(details[1]);
            } else {
                eventDescription.setText("No description available.");
            }
        } else {
            eventName.setText("Event name not provided");
            eventDescription.setText("No description available.");
        }

        leaveButton.setVisibility(View.GONE);

        checkIfUserIsInWaitingList();

        // Set onClick listeners for the buttons
        joinButton.setOnClickListener(v -> joinEvent(eventId));
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> waitingList = (List<String>) documentSnapshot.get("waitingList");

                        if (waitingList != null && waitingList.contains(entrant.getUserId())) {
                            joinButton.setVisibility(View.GONE);
                            leaveButton.setVisibility(View.VISIBLE);
                        } else {
                            // User is not in the waiting list, show join button
                            joinButton.setVisibility(View.VISIBLE);
                            leaveButton.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(EventDetailsActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to check waiting list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void joinEvent(String eventId) {
        if (eventId == null || entrant.getUserId() == null) {
            Toast.makeText(this, "Failed to join the event. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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