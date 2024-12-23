package com.example.carbon_project.Controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EventDetailsActivity extends AppCompatActivity {

    // UI elements
    private ImageView eventImage;
    private TextView eventName, eventDescription, eventCapacity, eventDate, eventOrganizer;
    private Button joinButton, leaveButton, enrollButton;

    // Firebase and data objects
    private FirebaseFirestore db;
    private String userId;
    private Event event;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        eventImage = findViewById(R.id.event_image);
        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        eventCapacity = findViewById(R.id.event_capacity);
        eventDate = findViewById(R.id.event_date);
        eventOrganizer = findViewById(R.id.event_organizer);
        joinButton = findViewById(R.id.join_event_button);
        leaveButton = findViewById(R.id.leave_event_button);
        enrollButton = findViewById(R.id.enroll_event_button);

        setupBottomNavigation();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            Toast.makeText(this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            finish();
        }

        userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        db = FirebaseFirestore.getInstance();

        loadEventData(eventId);

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

    // Load event data from Firestore
    private void loadEventData(String eventId) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> eventData = documentSnapshot.getData();
                        event = new Event(eventData);
                        if (event.getOrganizerId() != null) {
                            db.collection("users").document(event.getOrganizerId()).get()
                                    .addOnSuccessListener(documentSnapshot2 -> {
                                        if (documentSnapshot2.exists()) {
                                            setUpUI(documentSnapshot2.getString("name"));
                                        }
                                    });
                        } else {
                            setUpUI(null);
                        }
                    } else {
                        showError("Event details not found");
                    }
                })
                .addOnFailureListener(e -> showError("Error retrieving event details: " + e.getMessage()));
    }

    // Set up UI elements
    private void setUpUI(String organizerName) {
        if (event.getEventPosterUrl() != null) {
            // Set Image Here
        }
        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());
        int currentJoined = event.getWaitingList().size() + event.getSelectedList().size() + event.getEnrolledList().size();
        eventCapacity.setText("Capacity: " + currentJoined + "/" + event.getCapacity());
        eventDate.setText("Date: " + event.getStartDate() + " to " + event.getEndDate());

        if (organizerName != null) {
            eventOrganizer.setText("Organizer: " + organizerName);
        }

        // Populate views
        if (event.getEventPosterUrl() != null && !event.getEventPosterUrl().isEmpty()) {
            Picasso.get()
                    .load(event.getEventPosterUrl())
                    .into(eventImage);
        }

        updateButtonVisibility();

        // Set button click listeners
        joinButton.setOnClickListener(v -> {
            if (event.isGeolocationRequired()) {
                promptGeolocationConsent(() -> handleJoinEvent());
            } else {
                handleJoinEvent();
            }
        });
        leaveButton.setOnClickListener(v -> handleLeaveEvent());
        enrollButton.setOnClickListener(v -> handleEnrollEvent());
    }

    // Update button visibility based on user's list status
    private void updateButtonVisibility() {
        List<String> waitingList = event.getWaitingList();
        List<String> selectedList = event.getSelectedList();
        List<String> canceledList = event.getCanceledList();
        List<String> enrolledList = event.getEnrolledList();

        if (waitingList.contains(userId)) {
            // User in waitingList
            setButtonVisibility(false, true, false);
        } else if (selectedList.contains(userId)) {
            // User in selectedList
            setButtonVisibility(false, true, true);
        } else if (canceledList.contains(userId)) {
            // User in canceledList
            setButtonVisibility(true, false, false);
        } else if (enrolledList.contains(userId)) {
            // User in enrolledList
            setButtonVisibility(false, false, false);
            Toast.makeText(this, "You are already enrolled!", Toast.LENGTH_SHORT).show();
        } else {
            // User not in any list
            setButtonVisibility(true, false, false);
        }
    }

    // Helper to set button visibility
    private void setButtonVisibility(boolean showJoin, boolean showLeave, boolean showEnroll) {
        joinButton.setVisibility(showJoin ? View.VISIBLE : View.GONE);
        leaveButton.setVisibility(showLeave ? View.VISIBLE : View.GONE);
        enrollButton.setVisibility(showEnroll ? View.VISIBLE : View.GONE);
    }

    // Handle join event logic
    private void handleJoinEvent() {
        if (event.getCanceledList().contains(userId)) {
            Toast.makeText(this, "You cannot rejoin an event that has been canceled", Toast.LENGTH_SHORT).show();
        } else {
            updateList(null, "waitingList");
            db.collection("users").document(userId)
                    .update("joinedEvents", FieldValue.arrayUnion(event.getEventId()));
        }
    }

    // Handle leave event logic
    private void handleLeaveEvent() {
        if (event.getWaitingList().contains(userId)) {
            updateList("waitingList", null);
        } else if (event.getSelectedList().contains(userId)) {
            updateList("selectedList", "canceledList");
        }
        db.collection("users").document(userId)
                .update("joinedEvents", FieldValue.arrayRemove(event.getEventId()));
    }

    // Handle enroll event logic
    private void handleEnrollEvent() {
        if (event.getSelectedList().contains(userId)) {
            updateList("selectedList", "enrolledList");
        }
    }

    // Update Firestore lists
    private void updateList(String removeFromList, String addToList) {
        if (removeFromList != null) {
            db.collection("events").document(event.getEventId())
                    .update(removeFromList, FieldValue.arrayRemove(userId));
        }
        if (addToList != null) {
            db.collection("events").document(event.getEventId())
                    .update(addToList, FieldValue.arrayUnion(userId));
        }
        recreate();
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

    // Show error message
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Prompt for geolocation consent
    private void promptGeolocationConsent(Runnable onConsent) {
        new AlertDialog.Builder(this)
                .setTitle("Location Sharing Required")
                .setMessage("This event requires you to share your location. Do you want to proceed?")
                .setPositiveButton("OK", (dialog, which) -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getLocationAndUpdate();
                        onConsent.run();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(this, "Location sharing is required to join this event.", Toast.LENGTH_SHORT).show())
                .show();
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndUpdate();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Update waiting list with location data
    private void getLocationAndUpdate() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Create a map to hold the user's data, including userId and location
                        Map<String, Object> userLocation = new HashMap<>();
                        userLocation.put("latitude", latitude);
                        userLocation.put("longitude", longitude);

                        // Add this user's location to the waiting list in Firestore
                        db.collection("events").document(event.getEventId())
                                .update("geoLocations", FieldValue.arrayUnion(userLocation));
                    } else {
                        showError("Unable to retrieve location.");
                    }
                })
                .addOnFailureListener(e -> showError("Failed to get location: " + e.getMessage()));
    }
}

