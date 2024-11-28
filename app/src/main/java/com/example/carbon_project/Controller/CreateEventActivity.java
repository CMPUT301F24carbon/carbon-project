package com.example.carbon_project.Controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreateEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription, etEventCapacity;
    private TextView tvStartDate, tvEndDate;
    private Button btnSaveEvent;
    private CheckBox cbGeolocationRequired;
    private Spinner spFacility;
    private FirebaseFirestore db;
    private String organizerId;
    private ArrayList<Facility> facilityList;

    // Variables for the date picker
    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();

        organizerId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize views
        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventCapacity = findViewById(R.id.etEventCapacity);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        cbGeolocationRequired = findViewById(R.id.cbGeolocationRequired);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);
        spFacility = findViewById(R.id.spinnerFacility);

        // Initialize the facility list
        facilityList = new ArrayList<>();

        tvStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        tvEndDate.setOnClickListener(v -> showDatePickerDialog(false));

        btnSaveEvent.setOnClickListener(v -> createAndSaveEvent());

        fetchFacilitiesForOrganizer(organizerId);
    }

    private void showDatePickerDialog(boolean isStartDate) {
        // Initialize the calendar for the date picker dialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            if (isStartDate) {
                startYear = year1;
                startMonth = month1;
                startDay = dayOfMonth;
                tvStartDate.setText(formatDate(startYear, startMonth, startDay));
            } else {
                endYear = year1;
                endMonth = month1;
                endDay = dayOfMonth;
                tvEndDate.setText(formatDate(endYear, endMonth, endDay));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        // Format the date as YYYY-MM-DD
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void fetchFacilitiesForOrganizer(String organizerId) {
        // Query Firestore for facilities where the organizerId matches the logged-in user
        db.collection("facilities")
                .whereEqualTo("organizerId", organizerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Facility facility = document.toObject(Facility.class);
                            facilityList.add(facility);
                        }
                        populateFacilitySpinner();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error fetching facilities: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void populateFacilitySpinner() {
        ArrayList<String> facilityNames = new ArrayList<>();
        for (Facility facility : facilityList) {
            facilityNames.add(facility.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFacility.setAdapter(adapter);
    }

    private void createAndSaveEvent() {
        // Validate inputs
        String eventName = etEventName.getText().toString().trim();
        String eventDescription = etEventDescription.getText().toString().trim();
        int eventCapacity;
        try {
            eventCapacity = Integer.parseInt(etEventCapacity.getText().toString().trim());
        } catch (NumberFormatException e) {
            etEventCapacity.setError("Invalid capacity");
            return;
        }

        // Check if dates are selected
        if (tvStartDate.getText().toString().isEmpty() || tvEndDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the geolocation requirement
        boolean geolocationRequired = cbGeolocationRequired.isChecked();

        // Get the selected facility from the spinner
        int selectedFacilityPosition = spFacility.getSelectedItemPosition();
        if (selectedFacilityPosition == -1) {
            Toast.makeText(this, "Please select a facility", Toast.LENGTH_SHORT).show();
            return;
        }

        Facility selectedFacility = facilityList.get(selectedFacilityPosition);

        String eventId = UUID.randomUUID().toString();
        String qrCodeUrl = generateQRCodeForEvent(eventId);

        // Event poster URL (assuming the organizer uploads an image)
        String eventPosterUrl = "default_poster_url";
        Event event = new Event(eventId, eventName, eventDescription, organizerId, eventCapacity, geolocationRequired, tvStartDate.getText().toString(), tvEndDate.getText().toString(), eventPosterUrl, qrCodeUrl, selectedFacility);

        // Save the event to Firestore
        db.collection("events").document(eventId).set(event.toMap())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateEventActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error creating event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Example method to generate a QR code URL (replace with actual QR code generation logic)
    private String generateQRCodeForEvent(String eventId) {
        // Generate and return a QR code URL (example URL, replace with actual logic)
        return "https://example.com/qr-code/" + eventId;
    }
}