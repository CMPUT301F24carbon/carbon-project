package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button manageUsersButton;
    private Button manageEventsButton;
    private Button myProfileButton;

    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        admin = (Admin) getIntent().getSerializableExtra("userObject");

        if (admin == null) {
            Toast.makeText(this, "Admin data is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize the buttons
        manageUsersButton = findViewById(R.id.manage_users_button);
        manageEventsButton = findViewById(R.id.manage_events_button);
        myProfileButton = findViewById(R.id.my_profile_button);

        manageUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageUsersActivity.class);
            intent.putExtra("admin", admin);
            startActivity(intent);
        });

        manageEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, EventListActivity.class);
            intent.putExtra("admin", admin);
            startActivity(intent);
        });

        myProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ProfileActivity.class);
            intent.putExtra("userObject", admin);
            startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


