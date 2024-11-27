package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * FacilityListActivity is an activity that displays a list of facilities using a RecyclerView.
 * It sets up the RecyclerView with a FacilityAdapter to display the facility details.
 */
public class FacilityListActivity extends AppCompatActivity {

    private RecyclerView facilityRecyclerView;
    private FacilityAdapter facilityAdapter;
    private ArrayList<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_list);

        // Bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_facility_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_event_list) {
                    startActivity(new Intent(getApplicationContext(), EventListActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_facility_list) {
                    return true;
                }
                if (item.getItemId() == R.id.navigation_add_circle) {
                    startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_person) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

        // Initialize RecyclerView
        facilityRecyclerView = findViewById(R.id.facilityRecyclerView);
        facilityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of all facilities from the FacilityManager
        facilities = FacilityManager.getInstance().getFacilities();

        // Show a message if no facilities are available
        if (facilities.isEmpty()) {
            Toast.makeText(this, "No facilities available", Toast.LENGTH_SHORT).show();
        }

        // Initialize the adapter and set it to the RecyclerView
        facilityAdapter = new FacilityAdapter(facilities);
        facilityRecyclerView.setAdapter(facilityAdapter);
    }
}