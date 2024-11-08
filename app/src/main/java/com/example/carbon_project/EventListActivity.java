package com.example.carbon_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * EventListActivity is an activity that displays a list of events using a RecyclerView.
 * It sets up the RecyclerView with an EventAdapter to display the event details.
 */
public class EventListActivity extends AppCompatActivity {

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Initialize RecyclerView
        eventRecyclerView = findViewById(R.id.recyclerView_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of events from the EventManager
        events = EventManager.getInstance().getEvents();

        // Show a message if no events are available
        if (events.isEmpty()) {
            Toast.makeText(this, "No events available", Toast.LENGTH_SHORT).show();
        }

        // Set up the EventAdapter and assign it to the RecyclerView
        eventAdapter = new EventAdapter(events);
        eventRecyclerView.setAdapter(eventAdapter);
    }
}