package com.example.carbon_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditEventFragment extends DialogFragment {

    public interface EditEventDialogListener {
        void updateEvent(Event event, int position);
    }

    private EditText editEventName, editCapacity, editEventStartDate, editEventEndDate, editEventDescription;
    private EditEventDialogListener listener;
    private Event eventToEdit;
    private int position = -1;

    public void setEventToEdit(Event event, int position) {
        this.eventToEdit = event;
        this.position = position;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditEventDialogListener) {
            listener = (EditEventDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement EditEventDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_event, null);

        // Initialize views
        editEventName = view.findViewById(R.id.editEventName);
        editCapacity = view.findViewById(R.id.editEventCapacity);
        editEventStartDate = view.findViewById(R.id.editEventStartDate);
        editEventEndDate = view.findViewById(R.id.editEventEndDate);
        editEventDescription = view.findViewById(R.id.editEventDescription);

        // Set existing event details if available
        if (eventToEdit != null) {
            editEventName.setText(eventToEdit.getName());
            editCapacity.setText(String.valueOf(eventToEdit.getCapacity()));
            editEventStartDate.setText(eventToEdit.getStartDate());
            editEventEndDate.setText(eventToEdit.getEndDate());
            //editEventDescription.setText(eventToEdit.getDescription());
        }

        // DatePickerDialog for Start Date
        editEventStartDate.setOnClickListener(v -> showDatePickerDialog(editEventStartDate));

        // DatePickerDialog for End Date
        editEventEndDate.setOnClickListener(v -> showDatePickerDialog(editEventEndDate));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        return builder
                .setView(view)
                .setTitle("Edit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String eventName = editEventName.getText().toString().trim();
                    String capacityText = editCapacity.getText().toString().trim();
                    String startDate = editEventStartDate.getText().toString().trim();
                    String endDate = editEventEndDate.getText().toString().trim();
                    String description = editEventDescription.getText().toString().trim();

                    // Validate inputs
                    if (!validateInputs(eventName, capacityText, startDate, endDate)) {
                        return;
                    }

                    int capacity = Integer.parseInt(capacityText);

                    // Update event if valid
                    if (eventToEdit != null) {
                        eventToEdit.setName(eventName);
                        eventToEdit.setCapacity(capacity);
                        eventToEdit.setStartDate(startDate);
                        eventToEdit.setEndDate(endDate);
                        //eventToEdit.setDescription(description);
                        listener.updateEvent(eventToEdit, position);

                        // Call the Firestore update method
                        updateEventInFirestore(eventToEdit);
                    }
                }).create();
    }

    private void updateEventInFirestore(Event event) {
        String eventId = event.getEventId();

        // Firestore reference to the specific document
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventsRef = db.collection("events");

        DocumentReference eventRef = eventsRef.document(eventId);

        // Create a map of the fields you want to update
        Map<String, Object> eventUpdates = new HashMap<>();
        eventUpdates.put("name", event.getName());
        eventUpdates.put("capacity", event.getCapacity());
        eventUpdates.put("startDate", event.getStartDate());
        eventUpdates.put("endDate", event.getEndDate());
        //eventUpdates.put("description", event.getDescription());

        // Update the event in Firestore
        eventRef.update(eventUpdates)
                .addOnSuccessListener(aVoid -> {
                    showToast("Event updated successfully");
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to update event: " + e.getMessage());
                });
    }

    // Show DatePickerDialog
    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year1;
                    dateEditText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Validate user inputs
    private boolean validateInputs(String eventName, String capacityText, String startDate, String endDate) {
        if (eventName.isEmpty()) {
            showToast("Event name cannot be empty.");
            return false;
        }
        if (capacityText.isEmpty()) {
            showToast("Capacity cannot be empty.");
            return false;
        }
        if (startDate.isEmpty()) {
            showToast("Start date cannot be empty.");
            return false;
        }
        if (endDate.isEmpty()) {
            showToast("End date cannot be empty.");
            return false;
        }
        try {
            Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            showToast("Capacity must be a valid number.");
            return false;
        }
        return true;
    }

    // Show a Toast message
    private void showToast(String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}