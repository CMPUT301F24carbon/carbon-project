package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * The OrganizerCreateFacilityActivity class is an activity that allows an organizer to create a facility.
 */
public class OrganizerCreateFacilityActivity extends AppCompatActivity {

    private EditText facilityNameEditText;
    private EditText locationEditText;
    private EditText capacityEditText;
    private Button createFacilityButton;
    private String organizerId;

    private FirebaseFirestore db;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_create_facility);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        facilityNameEditText = findViewById(R.id.facility_name_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);
        capacityEditText = findViewById(R.id.capacity_edit_text);
        createFacilityButton = findViewById(R.id.create_facility_button);

        createFacilityButton.setOnClickListener(v -> {
            // Get the input values
            String facilityName = facilityNameEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();
            int capacity = Integer.parseInt(capacityEditText.getText().toString().trim());

            // Create a Facility object & Save
            String facilityId = UUID.randomUUID().toString();
            organizerId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Facility newFacility = new Facility(facilityId, facilityName, location, capacity, organizerId);
            saveFacilityToDatabase(newFacility);
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

    /**
     * Handles the back button click event.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Save a facility to the database.
     * @param facility
     */
    private void saveFacilityToDatabase(Facility facility) {
        db.collection("facilities")
                .document(facility.getFacilityId())
                .set(facility)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OrganizerCreateFacilityActivity.this, "Facility created successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerCreateFacilityActivity.this, "Error creating facility.", Toast.LENGTH_SHORT).show();
                });
    }
}
