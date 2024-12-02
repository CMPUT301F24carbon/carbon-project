package com.example.carbon_project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * The EditFacilityFragment class represents a dialog fragment for editing facility details.
 */
public class EditFacilityFragment extends DialogFragment {

    /**
     * The EditFacilityDialogListener interface defines a callback method for updating a facility.
     */
    public interface EditFacilityDialogListener {
        void updateFacility(Facility facility, int position);
    }

    private EditText editFacilityName, editFacilityLocation, editFacilityCapacity;
    private EditFacilityDialogListener listener;
    private com.example.carbon_project.Model.Facility facilityToEdit;
    private int position = -1;

    /**
     * Sets the facility to be edited.
     * @param facility
     */
    public void setFacilityToEdit(Facility facility) {
        this.facilityToEdit = facility;
    }

    /**
     * Called when the fragment is attached to the activity.
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditFacilityDialogListener) {
            listener = (EditFacilityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement EditFacilityDialogListener");
        }
    }

    /**
     * Called to create the dialog fragment.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_facility, null);

        // Initialize views
        editFacilityName = view.findViewById(R.id.editFacilityName);
        editFacilityLocation = view.findViewById(R.id.editFacilityLocation);
        editFacilityCapacity = view.findViewById(R.id.editFacilityCapacity);

        // Set existing facility details if available
        if (facilityToEdit != null) {
            editFacilityName.setText(facilityToEdit.getName());
            editFacilityLocation.setText(facilityToEdit.getLocation());
            editFacilityCapacity.setText(String.valueOf(facilityToEdit.getCapacity()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        return builder
                .setView(view)
                .setTitle("Edit Facility")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String facilityName = editFacilityName.getText().toString().trim();
                    String facilityLocation = editFacilityLocation.getText().toString().trim();
                    String capacityText = editFacilityCapacity.getText().toString().trim();

                    // Validate inputs
                    if (!validateInputs(facilityName, facilityLocation, capacityText)) {
                        return;
                    }

                    int capacity = Integer.parseInt(capacityText);

                    // Update facility if valid
                    if (facilityToEdit != null) {
                        facilityToEdit.setName(facilityName);
                        facilityToEdit.setLocation(facilityLocation);
                        facilityToEdit.setCapacity(capacity);

                        // Notify listener to update the facility list
                        listener.updateFacility(facilityToEdit, position);
                        updateFacilityInFirestore(facilityToEdit);
                    }
                }).create();
    }

    private boolean validateInputs(String facilityName, String facilityLocation, String capacityText) {
        if (facilityName.isEmpty()) {
            showToast("Facility name cannot be empty.");
            return false;
        }
        if (facilityLocation.isEmpty()) {
            showToast("Facility location cannot be empty.");
            return false;
        }
        if (capacityText.isEmpty()) {
            showToast("Capacity cannot be empty.");
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

    private void showToast(String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFacilityInFirestore(Facility facility) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference facilitiesRef = db.collection("facilities");

        DocumentReference facilityRef = facilitiesRef.document(facility.getFacilityId());

        // Create a map of the fields you want to update
        Map<String, Object> facilityUpdates = new HashMap<>();
        facilityUpdates.put("name", facility.getName());
        facilityUpdates.put("location", facility.getLocation());
        facilityUpdates.put("capacity", facility.getCapacity());

        // Update the facility in Firestore
        facilityRef.update(facilityUpdates)
                .addOnSuccessListener(aVoid -> showToast("Facility updated successfully"))
                .addOnFailureListener(e -> showToast("Failed to update facility: " + e.getMessage()));
    }

    /**
     * Sets the position of the facility in the list.
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }
}