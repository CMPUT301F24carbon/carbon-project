package com.example.carbon_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListActivity extends AppCompatActivity implements EventAdapter.OnEventClickListener, EditEventFragment.EditEventDialogListener {

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);  // Ensure this layout contains your RecyclerView

        eventRecyclerView = findViewById(R.id.recyclerView_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(events, this);
        eventRecyclerView.setAdapter(eventAdapter);

        // Set up swipe-to-delete functionality
        setupSwipeToDelete();

        // Fetch events from Firestore
        fetchEvents();
    }

    private void fetchEvents() {
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            events.clear();  // Clear any existing events

            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String eventId = document.getId();
                Event event = new Event(eventId);
                event.loadFromFirestore(new Event.DataLoadedCallback() {
                    @Override
                    public void onDataLoaded(HashMap<String, Object> eventData) {
                        // Add the event to the list and notify the adapter about the change
                        runOnUiThread(() -> {
                            events.add(event);
                            eventAdapter.notifyItemInserted(events.size() - 1);  // Notify RecyclerView about new item
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(EventListActivity.this, "Error loading event: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        }).addOnFailureListener(e -> {
            runOnUiThread(() -> {
                Toast.makeText(this, "Failed to retrieve events.", Toast.LENGTH_SHORT).show();
            });
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
                Event deletedEvent = events.get(position);

                // Show confirmation dialog before deleting
                new AlertDialog.Builder(EventListActivity.this)
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            // Delete event
                            deleteEvent(deletedEvent, position);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> eventAdapter.notifyItemChanged(position))
                        .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    // Paint for background color
                    Paint paint = new Paint();
                    paint.setColor(Color.RED); // Red background for delete action
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
        });
        itemTouchHelper.attachToRecyclerView(eventRecyclerView);
    }

    private void deleteEvent(Event event, int position) {
        // Remove event from Firestore
        FirebaseFirestore.getInstance().collection("events").document(event.getEventId()).delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the event from the list and update RecyclerView
                    events.remove(position);
                    eventAdapter.notifyItemRemoved(position);

                    // Show undo Snackbar
                    showUndoSnackbar(event, position);

                    // Optional: Update the UI or data accordingly
                    Toast.makeText(EventListActivity.this, "Event deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EventListActivity.this, "Error deleting event.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showUndoSnackbar(Event deletedEvent, int position) {
        Snackbar.make(eventRecyclerView, "Event deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // Restore the deleted event
                    events.add(position, deletedEvent);
                    eventAdapter.notifyItemInserted(position);
                    deletedEvent.uploadToFirestore();

                    // Optional: Scroll to the restored item
                    eventRecyclerView.scrollToPosition(position);
                }).show();
    }

    @Override
    public void onEventClick(Event event) {
        int position = events.indexOf(event);
        if (position != -1) {
            EditEventFragment editEventFragment = new EditEventFragment();
            editEventFragment.setEventToEdit(event);
            editEventFragment.setPosition(position);
            editEventFragment.show(getSupportFragmentManager(), "EditEventFragment");
        } else {
            Log.e("EventListActivity", "Event not found in list.");
        }
    }

    @Override
    public void updateEvent(Event event, int position) {
        events.set(position, event);
        eventAdapter.notifyItemChanged(position);
        event.uploadToFirestore();
    }
}
