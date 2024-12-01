package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrganizerUserListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView listNameTextView;
    private ListView listView;
    private ArrayList<String> eventList;
    private ArrayAdapter<String> adapter;
    private String eventId;
    private String listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_user_list);

        db = FirebaseFirestore.getInstance();
        listNameTextView = findViewById(R.id.list_name);
        listView = findViewById(R.id.event_list_view);

        // Get the event ID and list type from the intent
        eventId = getIntent().getStringExtra("eventId");
        listType = getIntent().getStringExtra("listType");

        if (listType.equals("waitingList")) {
            listNameTextView.setText("Waiting List");
        } else if (listType.equals("selectedList")) {
            listNameTextView.setText("Selected List");
        } else if (listType.equals("cancelledList")) {
            listNameTextView.setText("Cancelled List");
        } else if (listType.equals("enrolledList")) {
            listNameTextView.setText("Enrolled List");
        } else {
            finish(); // Close activity if listType is missing
        }

        eventList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventList);
        listView.setAdapter(adapter);

        fetchListData(eventId, listType);

        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
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

    private void fetchListData(String eventId, String listType) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ArrayList<String> userIds = (ArrayList<String>) documentSnapshot.get(listType);
                        eventList.clear();

                        if (userIds != null && !userIds.isEmpty()) {
                            // For each user ID in the list, fetch the user's name
                            for (String userId : userIds) {
                                fetchUserName(userId);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void fetchUserName(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(userDocument -> {
                    if (userDocument.exists()) {
                        String userName = userDocument.getString("name");
                        if (userName != null) {
                            eventList.add(userName);  // Add the user's name to the event list
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}