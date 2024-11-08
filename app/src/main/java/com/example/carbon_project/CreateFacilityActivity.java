package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

/**
 * CreateFacilityActivity is an activity that allows users to create a new facility.
 * It includes input fields for facility details and buttons to create the facility
 * or return to the previous screen.
 */
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

        // Initialize UI elements
        facilityNameInput = findViewById(R.id.facilityNameInput);
        locationInput = findViewById(R.id.locationInput);
        capacityInput = findViewById(R.id.capacityInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        createFacilityButton = findViewById(R.id.createFacilityButton);
        backButton = findViewById(R.id.backButton);

        // Set up button listeners
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

    /**
     * Gathers input data, validates it, and creates a new facility.
     * Displays a message if any fields are incomplete or invalid.
     */
    private void createFacility() {
        String facilityName = facilityNameInput.getText().toString().trim();
        String facilityLocation = locationInput.getText().toString().trim();
        String facilityCapacity = capacityInput.getText().toString().trim();
        String facilityDescription = descriptionInput.getText().toString().trim();

        // Validate input fields
        if (facilityName.isEmpty() || facilityLocation.isEmpty() || facilityCapacity.isEmpty() || facilityDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Handle number format exception
        int capacity;
        try {
            capacity = Integer.parseInt(facilityCapacity);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for capacity.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new facility and add it to the facility manager
        Facility newFacility = new Facility(generateUniqueId(), facilityName, facilityLocation, capacity, facilityDescription);
        FacilityManager.getInstance().addFacility(newFacility);

        // Navigate to FacilityListActivity and display success message
        Intent resultIntent = new Intent(this, FacilityListActivity.class);
        startActivity(resultIntent);
        Toast.makeText(this, "Facility created successfully!", Toast.LENGTH_SHORT).show();

        // Clear input fields
        clearInputs();
    }

    /**
     * Generates a unique identifier for a new facility using UUID.
     * @return A unique facility ID string.
     */
    private String generateUniqueId() {
        return "FAC" + UUID.randomUUID().toString();
    }

    /**
     * Clears all input fields in the activity, resetting them to their default state.
     */
    private void clearInputs() {
        facilityNameInput.setText("");
        locationInput.setText("");
        capacityInput.setText("");
        descriptionInput.setText("");
    }
}