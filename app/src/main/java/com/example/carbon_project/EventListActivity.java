package com.example.carbon_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        eventRecyclerView = findViewById(R.id.recyclerView_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        events = EventManager.getInstance().getEvents();

        if (events.isEmpty()) {
            Toast.makeText(this, "No events available", Toast.LENGTH_SHORT).show();
        }

        eventAdapter = new EventAdapter(events);
        eventRecyclerView.setAdapter(eventAdapter);
    }
}
