package com.example.carbon_project;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
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

        // Initialize Firebase Firestore
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
                    facilityAdapter.setOnItemClickListener(new FacilityAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Facility facility, int position) {
                            // Open the EditFacilityFragment on item click
                            EditFacilityFragment editFacilityFragment = new EditFacilityFragment();
                            editFacilityFragment.setFacilityToEdit(facility);
                            editFacilityFragment.setPosition(position);
                            editFacilityFragment.show(getSupportFragmentManager(), "editFacility");
                        }
                    });

                    // Set up swipe-to-delete
                    setupSwipeToDelete();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerFacilitiesActivity.this, "Error fetching facilities", Toast.LENGTH_SHORT).show();
                });
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

                // Show confirmation dialog before deleting
                new AlertDialog.Builder(OrganizerFacilitiesActivity.this)
                        .setTitle("Delete Facility")
                        .setMessage("Are you sure you want to delete this facility?")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            // Facility confirmed for deletion
                            checkIfFacilityIsInUse(facilityToDelete, position);
                        })
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

                        // Paint for the "Delete" text
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

    private void checkIfFacilityIsInUse(Facility facility, int position) {
        String facilityId = facility.getFacilityId();  // Assuming facility has a unique ID

        db.collection("events")
                .whereEqualTo("facility.facilityId", facilityId) // Query using the embedded facilityId
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Facility is being used in an event, show a message
                        Toast.makeText(OrganizerFacilitiesActivity.this, "Cannot delete this facility, it is being used for an event.", Toast.LENGTH_SHORT).show();
                        facilityAdapter.notifyDataSetChanged();
                    } else {
                        deleteFacility(facility, position);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerFacilitiesActivity.this, "Error checking facility usage", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteFacility(Facility facility, int position) {
        db.collection("facilities")
                .document(facility.getFacilityId()) // Assuming facility has a unique ID
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Facility deleted, remove it from the list
                    facilityList.remove(position);
                    facilityAdapter.notifyItemRemoved(position);
                    Toast.makeText(OrganizerFacilitiesActivity.this, "Facility deleted successfully", Toast.LENGTH_SHORT).show();

                    // Show the undo Snackbar
                    showUndoSnackbar(facility, position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerFacilitiesActivity.this, "Error deleting facility", Toast.LENGTH_SHORT).show();
                });
    }

    private void showUndoSnackbar(Facility facility, int position) {
        // Store the deleted facility in a separate variable for undo
        lastDeletedFacility = facility;
        lastDeletedPosition = position;

        Snackbar.make(facilitiesRecyclerView, "Facility deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // Only add the facility back once
                    if (lastDeletedFacility != null && lastDeletedPosition >= 0) {
                        // Re-add the facility to the Firestore database
                        restoreFacility(lastDeletedFacility, lastDeletedPosition);
                    }
                }).show();
    }

    private void restoreFacility(Facility facility, int position) {
        // Update the Firestore document with the deleted facility
        db.collection("facilities")
                .document(facility.getFacilityId()) // Assuming facility has a unique ID
                .set(facility) // Restore the facility in Firestore
                .addOnSuccessListener(aVoid -> {
                    // Update the view
                    facilityList.add(position, facility);
                    facilityAdapter.notifyItemInserted(position);

                    // Clear the lastDeletedFacility and position to prevent duplicate additions
                    lastDeletedFacility = null;
                    lastDeletedPosition = -1;

                    Toast.makeText(OrganizerFacilitiesActivity.this, "Facility restored", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerFacilitiesActivity.this, "Error restoring facility", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void updateFacility(Facility facility, int position) {
        // Update the facility in the list
        facilityList.set(position, facility);
        facilityAdapter.notifyItemChanged(position);
    }
}