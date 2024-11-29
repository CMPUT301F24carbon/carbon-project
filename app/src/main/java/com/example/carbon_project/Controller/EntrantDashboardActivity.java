package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.R;

public class EntrantDashboardActivity extends AppCompatActivity {

    private Button viewEventsButton, myProfileButton, joinEventButton;

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
    }
}
