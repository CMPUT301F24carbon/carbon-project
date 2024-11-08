package com.example.carbon_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FacilityListActivity extends AppCompatActivity {

    private RecyclerView facilityRecyclerView;
    private FacilityAdapter facilityAdapter;
    private ArrayList<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_list);

        // Initialize RecyclerView
        facilityRecyclerView = findViewById(R.id.facilityRecyclerView);
        facilityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of all facilities from the FacilityManager
        facilities = FacilityManager.getInstance().getFacilities();

        if (facilities.isEmpty()) {
            Toast.makeText(this, "No facilities available", Toast.LENGTH_SHORT).show();
        }

        // Initialize the adapter and set it to the RecyclerView
        facilityAdapter = new FacilityAdapter(facilities);
        facilityRecyclerView.setAdapter(facilityAdapter);
    }
}