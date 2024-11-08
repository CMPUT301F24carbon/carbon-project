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

        facilityRecyclerView = findViewById(R.id.facilityRecyclerView);
        facilityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        facilities = new ArrayList<>();

        // Retrieve data from the intent
        String facilityName = getIntent().getStringExtra("facilityName");
        String facilityLocation = getIntent().getStringExtra("facilityLocation");
        int facilityCapacity = getIntent().getIntExtra("facilityCapacity", 0);
        String facilityDescription = getIntent().getStringExtra("facilityDescription");

        if (facilityName != null && facilityLocation != null && facilityDescription != null) {
            facilities.add(new Facility("1", facilityName, facilityLocation, facilityCapacity, facilityDescription));
        } else {
            Toast.makeText(this, "No facility data received", Toast.LENGTH_SHORT).show();
        }

        // Initialize the adapter and set it to the RecyclerView
        facilityAdapter = new FacilityAdapter(facilities);
        facilityRecyclerView.setAdapter(facilityAdapter);
    }
}