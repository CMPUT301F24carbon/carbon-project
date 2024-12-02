package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The EntrantDashboardActivity class is an activity that displays the dashboard for an entrant.
 */
public class EntrantDashboardActivity extends AppCompatActivity {

    private Button viewEventsButton, myProfileButton, joinEventButton, acceptEventButton;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_dashboard);

        Entrant entrant = (Entrant) getIntent().getSerializableExtra("userObject");

        if (entrant == null) {
            Toast.makeText(this, "Entrant data is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        viewEventsButton = findViewById(R.id.view_events_button);
        joinEventButton = findViewById(R.id.join_event_button);
        acceptEventButton = findViewById(R.id.accept_invitation_button);
        myProfileButton = findViewById(R.id.my_profile_button);

        viewEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantDashboardActivity.this, EntrantEventsListActivity.class);
            intent.putExtra("userObject", entrant);
            startActivity(intent);
        });

        myProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantDashboardActivity.this, ProfileActivity.class);
            intent.putExtra("userObject", entrant);
            startActivity(intent);
        });
      
        joinEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantDashboardActivity.this, EntrantJoinEventActivity.class);
            intent.putExtra("userObject", entrant);
            startActivity(intent);
        });

        acceptEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantDashboardActivity.this, EntrantAcceptEventActivity.class);
            intent.putExtra("userObject", entrant);
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

    /**
     * Handles the back button click event.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
