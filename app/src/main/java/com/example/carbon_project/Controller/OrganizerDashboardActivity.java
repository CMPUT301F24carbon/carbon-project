package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrganizerDashboardActivity extends AppCompatActivity {
    private Button createEventButton, viewEventButton, viewFacilitiesButton, createFacilityButton, myProfileButton;
    private Organizer organizer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        Organizer organizer = (Organizer) getIntent().getSerializableExtra("userObject");

        // Initialize the buttons
        createEventButton = findViewById(R.id.create_event_button);
        viewEventButton = findViewById(R.id.view_event_button);
        viewFacilitiesButton = findViewById(R.id.view_facilities_button);
        createFacilityButton = findViewById(R.id.create_facility_button);
        myProfileButton = findViewById(R.id.my_profile_button);

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

        myProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerDashboardActivity.this, ProfileActivity.class);
            intent.putExtra("userObject", organizer);
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
}
