package com.example.carbon_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class CreateFacilityActivity extends FragmentActivity {

    private EditText facilityNameInput;
    private EditText locationInput;
    private EditText capacityInput;
    private EditText descriptionInput;
    private Button createFacilityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_facility);

        // References of UI
        facilityNameInput = findViewById(R.id.facilityNameInput);
        locationInput = findViewById(R.id.locationInput);
        capacityInput = findViewById(R.id.capacityInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        createFacilityButton = findViewById(R.id.createFacilityButton);

        createFacilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFacility();
            }
        });
    }

    /**
     * Method to handle facility creation logic.
     */
    private void createFacility() {
        String facilityName = facilityNameInput.getText().toString().trim();
        String facilityLocation = locationInput.getText().toString().trim();
        String facilityCapacity = capacityInput.getText().toString().trim();
        String facilityDescription = descriptionInput.getText().toString().trim();

        // validation
        if (facilityName.isEmpty() || facilityLocation.isEmpty() || facilityCapacity.isEmpty() || facilityDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // capacity to integer
        int capacity;
        try {
            capacity = Integer.parseInt(facilityCapacity);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // new facility success message
        String successMessage = String.format("Facility '%s' created at '%s'", facilityName, facilityLocation);
        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();

        // clear feilds after creation
        clearInputFields();
    }

    private void clearInputFields() {
        facilityNameInput.setText("");
        locationInput.setText("");
        capacityInput.setText("");
        descriptionInput.setText("");
    }
}