package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.example.carbon_project.View.EditFacilityFragment;
import com.example.carbon_project.View.EventsAdapter;
import com.example.carbon_project.View.FacilityAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EntrantAcceptEventActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView eventsListView;
    private Entrant entrant;
    private ArrayList<String> eventIds;
    private ArrayList<String> eventNames;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        entrant = (Entrant) getIntent().getSerializableExtra("userObject");

        db = FirebaseFirestore.getInstance();

        eventsListView = findViewById(R.id.event_list_view);
        eventNames = new ArrayList<>();
        eventIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        eventsListView.setAdapter(adapter);


        db.collection("events")
                .whereArrayContains("selectedList", entrant.getUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        eventIds.add(document.getId());
                        eventNames.add(document.getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                });

        eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEventId = eventIds.get(position);

            // Create a dialog with menu options
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Action");

            // Define menu options
            String[] options = {"Accept Invitation", "Reject Invitation", "Cancel"};

            // Set the options in the dialog
            builder.setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0: // Accept Invitation
                        updateFirestore(selectedEventId, "selectedList", "enrolledList", entrant.getUserId());
                        eventIds.remove(position);
                        eventNames.remove(position);
                        adapter.notifyDataSetChanged();
                        break;

                    case 1: // Reject Invitation
                        updateFirestore(selectedEventId, "selectedList", "canceledList", entrant.getUserId());
                        eventIds.remove(position);
                        eventNames.remove(position);
                        adapter.notifyDataSetChanged();
                        break;

                    case 2: // Cancel
                        dialog.dismiss();
                        break;
                }
            });

            // Show the dialog
            builder.create().show();
        });

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

    private void updateFirestore(String eventId, String fromList, String toList, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("events").document(eventId);

        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(docRef);
                    List<String> from = (List<String>) snapshot.get(fromList);
                    List<String> to = (List<String>) snapshot.get(toList);

                    from.remove(userId);
                    to.add(userId);

                    transaction.update(docRef, fromList, from);
                    transaction.update(docRef, toList, to);
                    return null;
                }).addOnSuccessListener(aVoid -> Log.d("Firestore", "User moved successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error moving user", e));
    }
}
