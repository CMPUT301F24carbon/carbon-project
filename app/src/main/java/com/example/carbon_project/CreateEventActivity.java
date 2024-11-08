package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventName;
    private EditText eventCapacity;
    private EditText startDate;
    private EditText endDate;
    private EditText description;
    private CheckBox geolocationCheckbox;
    private Button publishButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        eventName = findViewById(R.id.eventNameInput);
        eventCapacity = findViewById(R.id.eventCapacityInput);
        startDate = findViewById(R.id.eventStartDateInput);
        endDate = findViewById(R.id.eventEndDateInput);
        description = findViewById(R.id.eventDescriptionInput);
        geolocationCheckbox = findViewById(R.id.eventCheckbox);
        publishButton = findViewById(R.id.publishButton);
        backButton = findViewById(R.id.backButton);


        // Publish button functionality
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        // Back button functionality
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainActivity and close CreateEventActivity
                Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });
    }

    /**
     * Creates a new event.
     */
    private void createEvent() {
        String event_name = eventName.getText().toString().trim();
        String event_capacity = eventCapacity.getText().toString().trim();
        String event_start = startDate.getText().toString().trim();
        String event_end = endDate.getText().toString().trim();
        String event_desc = description.getText().toString().trim();

        if (event_name.isEmpty() || event_capacity.isEmpty() || event_start.isEmpty() || event_end.isEmpty() || event_desc.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // capacity to integer
        int capacity;
        try {
            capacity = Integer.parseInt(event_capacity);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (geolocationCheckbox.isChecked()) {
            // Geolocation is enabled
            return;
        }

        // new facility success message
        String successMessage = String.format("'%s' event created", event_name);
        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();

        // clear feilds after creation
        eventName.setText("");
        eventCapacity.setText("");
        startDate.setText("");
        endDate.setText("");
        description.setText("");
    }

}