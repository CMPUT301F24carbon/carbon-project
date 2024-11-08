package com.example.carbon_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbon_project.Facility;
import com.example.carbon_project.FacilityAdapter;
import com.example.carbon_project.R;

import java.util.ArrayList;

public class FacilityListActivity extends AppCompatActivity {

    private RecyclerView facilityRecyclerView;
    private FacilityAdapter facilityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_list);  // Inflate the layout with RecyclerView

        // Find the RecyclerView by ID
        facilityRecyclerView = findViewById(R.id.facilityRecyclerView);

        // Set a LayoutManager (typically LinearLayoutManager for vertical lists)
        facilityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of facilities (you can replace this with dynamic data)
        ArrayList<Facility> facilities = new ArrayList<>();
        facilities.add(new Facility("1", "Facility One", "Location A", 100, "Description of Facility One"));
        facilities.add(new Facility("2", "Facility Two", "Location B", 200, "Description of Facility Two"));

        // Initialize the adapter and set it to the RecyclerView
        facilityAdapter = new FacilityAdapter(facilities);
        facilityRecyclerView.setAdapter(facilityAdapter);
    }
}