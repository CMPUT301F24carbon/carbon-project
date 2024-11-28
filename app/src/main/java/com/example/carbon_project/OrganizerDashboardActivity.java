package com.example.carbon_project;

/*
public class OrganizerDashboardActivity extends AppCompatActivity {

    private Button createEventButton;
    private Button viewEventButton;
    private Button viewFacilitiesButton;
    private Button createFacilityButton;

    private Organizer organizer;  // Store the Organizer object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        organizer = (Organizer) getIntent().getSerializableExtra("organizer");

        // Initialize the buttons
        createEventButton = findViewById(R.id.create_event_button);
        viewEventButton = findViewById(R.id.view_event_button);
        viewFacilitiesButton = findViewById(R.id.view_facilities_button);
        createFacilityButton = findViewById(R.id.create_facility_button);

        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, CreateEventActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });

        viewEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, ViewEventsActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });

        viewFacilitiesButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, OrganizerFacilitiesActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });

        createFacilityButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, OrganizerCreateFacilityActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });
    }
}
*/

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class OrganizerDashboardActivity extends AppCompatActivity {

    private Button createEventButton;
    private Button viewEventButton;
    private Button viewFacilitiesButton;
    private Button createFacilityButton;

    private Organizer organizer;  // Store the Organizer object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        organizer = (Organizer) getIntent().getSerializableExtra("organizer");

        // Initialize the buttons
        createEventButton = findViewById(R.id.create_event_button);
        viewEventButton = findViewById(R.id.view_event_button);
        viewFacilitiesButton = findViewById(R.id.view_facilities_button);
        createFacilityButton = findViewById(R.id.create_facility_button);

        // Create Event Button click listener
        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, CreateEventActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });

        // View Event Button click listener
        viewEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, ViewEventsActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });

        // View Facilities Button click listener - Replace Activity with Fragment
        viewFacilitiesButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, OrganizerFacilitiesActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });

        createFacilityButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, OrganizerCreateFacilityActivity.class);
            intent.putExtra("organizer", organizer);
            startActivity(intent);
        });
    }
}
