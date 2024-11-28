package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;


public class EventListActivity extends AppCompatActivity {
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventListView = findViewById(R.id.event_list_view);
        loadEvents();

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra("event_id", id);
            startActivity(intent);
        });
    }

    private void loadEvents() {
        // Firebase logic to fetch events and populate ListView
    }
}
