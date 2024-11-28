package com.example.carbon_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CreateFacilityActivity extends AppCompatActivity {
    private EditText etFacilityName, etFacilityLocation, etFacilityCapacity;
    private Button btnCreateFacility;
    private FirebaseFirestore db;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_facility);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        organizerId = auth.getCurrentUser().getUid();

        // Initialize UI elements
        etFacilityName = findViewById(R.id.etFacilityName);
        etFacilityLocation = findViewById(R.id.etFacilityLocation);
        etFacilityCapacity = findViewById(R.id.etFacilityCapacity);
        btnCreateFacility = findViewById(R.id.btnCreateFacility);

        // Set up button click listener
        btnCreateFacility.setOnClickListener(v -> createFacility());
    }

    private void createFacility() {
        String facilityName = etFacilityName.getText().toString().trim();
        String facilityLocation = etFacilityLocation.getText().toString().trim();
        int facilityCapacity;

        // Input validation
        try {
            facilityCapacity = Integer.parseInt(etFacilityCapacity.getText().toString().trim());
        } catch (NumberFormatException e) {
            etFacilityCapacity.setError("Please enter a valid capacity");
            return;
        }

        // Check if any input is missing
        if (facilityName.isEmpty() || facilityLocation.isEmpty() || facilityCapacity <= 0) {
            Toast.makeText(this, "All fields are required and capacity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        String facilityId = UUID.randomUUID().toString();
        // Create a new Facility object
        Facility facility = new Facility(facilityId, facilityName, facilityLocation, facilityCapacity, organizerId);

        // Save the facility to Firestore
        db.collection("facilities").document(facilityId).set(facility)
                .addOnSuccessListener(aVoid -> {
                    db.collection("organizers").document(organizerId)
                            .update("facilityIds", FieldValue.arrayUnion(facilityId))
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(CreateFacilityActivity.this, "Facility created successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CreateFacilityActivity.this, "Error adding facility to organizer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateFacilityActivity.this, "Error creating facility: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
