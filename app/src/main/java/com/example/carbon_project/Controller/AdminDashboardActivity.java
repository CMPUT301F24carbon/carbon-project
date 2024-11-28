package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.R;

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
    }
}


