package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EntrantDashboardActivity extends AppCompatActivity {

    private Entrant entrant;
    private Button viewEventsButton, myProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_dashboard);

        entrant = (Entrant) getIntent().getSerializableExtra("entrantObject");

        if (entrant == null) {
            Toast.makeText(this, "Entrant data is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        viewEventsButton = findViewById(R.id.view_events_button);
        myProfileButton = findViewById(R.id.my_profile_button);

        viewEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantDashboardActivity.this, EntrantEventsListActivity.class);
            intent.putExtra("entrantObject", entrant);
            startActivity(intent);
        });

        myProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantDashboardActivity.this, ProfileActivity.class);
            intent.putExtra("entrantObject", entrant);
            startActivity(intent);
        });
    }
}
