package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Notification;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class OrganizerEventListDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView eventName, eventDescription, eventCapacity, eventStart, eventEnd;
    private ImageView eventImage;
    private Button viewMapButton, viewWaitingListButton, viewSelectedListButton, viewCancelledListButton, viewEnrolledListButton, lotteryButton, notifButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind views
        eventImage = findViewById(R.id.event_image);
        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        eventCapacity = findViewById(R.id.event_capacity);
        eventStart = findViewById(R.id.event_start_date);
        eventEnd = findViewById(R.id.event_end_date);
        eventImage = findViewById(R.id.event_image);
        notifButton = findViewById(R.id.notif_button);
        lotteryButton = findViewById(R.id.lottery_button);

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
        notifButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrganizerSendNotifActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });
        lotteryButton.setOnClickListener(v -> {
            if (eventId != null) {
                runLottery(eventId);
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
                        if (posterUrl != null && !posterUrl.isEmpty()) {
                            Picasso.get()
                                    .load(posterUrl)
                                    .into(eventImage);
                        }

                        eventName.setText(name);
                        eventDescription.setText(description);
                        eventCapacity.setText("Capacity: " + capacity);
                        eventStart.setText("Start: " + startDate);
                        eventEnd.setText("End: " + endDate);

                        // Handle geolocationRequired field
                        if (geolocationRequired != null && geolocationRequired) {
                            viewMapButton.setVisibility(View.VISIBLE);
                        } else {
                            viewMapButton.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching event details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void runLottery(String eventId) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ArrayList<String> waitingList = (ArrayList<String>) documentSnapshot.get("waitingList");
                        ArrayList<String> selectedList = (ArrayList<String>) documentSnapshot.get("selectedList");
                        int capacity = documentSnapshot.getLong("capacity").intValue();

                        if (waitingList != null && !waitingList.isEmpty()) {
                            if (selectedList == null) {
                                selectedList = new ArrayList<>();
                            }

                            if(capacity >= waitingList.size() + selectedList.size() || capacity==0){
                                selectedList.addAll(waitingList);
                                waitingList.clear();
                            }
                            else {
                                while(selectedList.size()<capacity && !waitingList.isEmpty()){
                                    // Pick a random person from the waiting list
                                    Random random = new Random();
                                    System.out.println(waitingList.size());
                                    int randomIndex = random.nextInt(waitingList.size());
                                    String selectedPerson = waitingList.remove(randomIndex);
                                    selectedList.add(selectedPerson);
                                }
                            }

                            // Update Firestore with the modified lists
                            db.collection("events").document(eventId)
                                    .update("waitingList", waitingList, "selectedList", selectedList)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "People selected and moved to Selected List.", Toast.LENGTH_SHORT).show();
                                        Notification.sendToSelected(eventId, "You have been selected to participate in an event." +
                                                " Go to the accept invitation page to enroll");
                                        Notification.sendToWating(eventId, "You have not been select to participate in " + eventName.getText().toString() +"."
                                                + " Don't worry if somebody drops out you will get another chance to participate");
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error updating lists: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Waiting list is empty.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error retrieving event details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}