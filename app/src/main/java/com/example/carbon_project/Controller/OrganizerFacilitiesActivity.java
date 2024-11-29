package com.example.carbon_project.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.R;
import com.example.carbon_project.View.EditFacilityFragment;
import com.example.carbon_project.View.FacilityAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
public class OrganizerFacilitiesActivity extends AppCompatActivity implements EditFacilityFragment.EditFacilityDialogListener {

    private RecyclerView facilitiesRecyclerView;
    private FacilityAdapter facilityAdapter;
    private List<Facility> facilityList;
    private FirebaseFirestore db;
    private String organizerId;

    private Facility lastDeletedFacility;
    private int lastDeletedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_facilities);

        // Initialize RecyclerView
        facilitiesRecyclerView = findViewById(R.id.rvFacilities);
        facilitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        Organizer organizer = (Organizer) getIntent().getSerializableExtra("organizer");

        if (organizer != null) {
            organizerId = organizer.getUserId();
        } else {
            organizerId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        db.collection("facilities")
                .whereEqualTo("organizerId", organizerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    facilityList = queryDocumentSnapshots.toObjects(Facility.class);

                    facilityAdapter = new FacilityAdapter(facilityList);
                    facilitiesRecyclerView.setAdapter(facilityAdapter);

                    // Set onClickListener for item click
                    facilityAdapter.setOnItemClickListener((facility, position) -> {
                        EditFacilityFragment editFacilityFragment = new EditFacilityFragment();
                        editFacilityFragment.setFacilityToEdit(facility);
                        editFacilityFragment.setPosition(position);
                        editFacilityFragment.show(getSupportFragmentManager(), "editFacility");
                    });

                    // Set up swipe-to-delete
                    setupSwipeToDelete();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerFacilitiesActivity.this, "Error fetching facilities", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No movement needed
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Facility facilityToDelete = facilityList.get(position);

                new AlertDialog.Builder(OrganizerFacilitiesActivity.this)
                        .setTitle("Delete Facility")
                        .setMessage("Are you sure you want to delete this facility and all its associated events?")
                        .setPositiveButton("Confirm", (dialog, which) -> deleteFacilityAndAssociatedEvents(facilityToDelete, position))
                        .setNegativeButton("Cancel", (dialog, which) -> facilityAdapter.notifyItemChanged(position))
                        .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    if (dX < 0) {
                        paint.setColor(Color.RED);
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), paint);

                        Paint textPaint = new Paint();
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextSize(40);
                        String deleteText = "Delete";
                        float textWidth = textPaint.measureText(deleteText);
                        c.drawText(deleteText, itemView.getRight() - textWidth - 20,
                                itemView.getTop() + (itemView.getHeight() / 2) + 12, textPaint);
                    }
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(facilitiesRecyclerView);
    }

    private void deleteFacilityAndAssociatedEvents(Facility facility, int position) {
        String facilityId = facility.getFacilityId();

        db.collection("events")
                .whereEqualTo("facility.facilityId", facilityId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("events").document(document.getId()).delete()
                                .addOnFailureListener(e ->
                                        Toast.makeText(OrganizerFacilitiesActivity.this, "Error deleting event: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    }

                    db.collection("facilities")
                            .document(facilityId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                facilityList.remove(position);
                                facilityAdapter.notifyItemRemoved(position);
                                Toast.makeText(OrganizerFacilitiesActivity.this, "Facility and associated events deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(OrganizerFacilitiesActivity.this, "Error deleting facility: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
                .addOnFailureListener(e ->
                        Toast.makeText(OrganizerFacilitiesActivity.this, "Error fetching associated events: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public void updateFacility(Facility facility, int position) {
        facilityList.set(position, facility);
        facilityAdapter.notifyItemChanged(position);
    }
}