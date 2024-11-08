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

        // Handle number format exception
        int capacity;
        try {
            capacity = Integer.parseInt(facilityCapacity);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for capacity.", Toast.LENGTH_SHORT).show();
            return;
        }

        Facility newFacility = new Facility(generateUniqueId(), facilityName, facilityLocation, capacity, facilityDescription);
        FacilityManager.getInstance().addFacility(newFacility);

        Intent resultIntent = new Intent(this, FacilityListActivity.class);
        startActivity(resultIntent);

        Toast.makeText(this, "Facility created successfully!", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    private String generateUniqueId() {
        return "FAC" + System.currentTimeMillis();
    }

    private void clearInputs() {
        facilityNameInput.setText("");
        locationInput.setText("");
        capacityInput.setText("");
        descriptionInput.setText("");
    }
}