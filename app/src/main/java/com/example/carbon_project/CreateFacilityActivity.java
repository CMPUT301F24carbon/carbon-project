package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateFacilityActivity extends AppCompatActivity {
    private EditText facilityNameInput;
    private EditText locationInput;
    private EditText capacityInput;
    private EditText descriptionInput;
    private Button createFacilityButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_facility);

        facilityNameInput = findViewById(R.id.facilityNameInput);
        locationInput = findViewById(R.id.locationInput);
        capacityInput = findViewById(R.id.capacityInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        createFacilityButton = findViewById(R.id.createFacilityButton);
        backButton = findViewById(R.id.backButton);

        createFacilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFacility();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createFacility() {
        String facilityName = facilityNameInput.getText().toString().trim();
        String facilityLocation = locationInput.getText().toString().trim();
        String facilityCapacity = capacityInput.getText().toString().trim();
        String facilityDescription = descriptionInput.getText().toString().trim();

        if (facilityName.isEmpty() || facilityLocation.isEmpty() || facilityCapacity.isEmpty() || facilityDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the capacity as an integer
        int capacity = Integer.parseInt(facilityCapacity);

        // Create the new facility object
        Facility newFacility = new Facility(generateUniqueId(), facilityName, facilityLocation, capacity, facilityDescription);

        // Create an intent to pass the new facility data to the next activity
        Intent resultIntent = new Intent(this, FacilityListActivity.class);
        // Pass facility data through intent extras
        resultIntent.putExtra("facilityName", newFacility.getName());
        resultIntent.putExtra("facilityLocation", newFacility.getLocation());
        resultIntent.putExtra("facilityCapacity", newFacility.getCapacity());
        resultIntent.putExtra("facilityDescription", newFacility.getDescription());
        resultIntent.putExtra("facilityId", newFacility.getFacilityId());

        // Start the next activity
        startActivity(resultIntent);
    }

    private String generateUniqueId() {
        return "FAC" + System.currentTimeMillis();
    }
}