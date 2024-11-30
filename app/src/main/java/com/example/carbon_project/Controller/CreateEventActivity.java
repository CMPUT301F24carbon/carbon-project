package com.example.carbon_project.Controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
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
        byte[] qrCode = generateQRCodeForEvent(eventId);
        String qrUri = byteArrayToUri(qrCode);

        // Event poster URL (assuming the organizer uploads an image)
        String eventPosterUrl = "default_poster_url";
        Event event = new Event(eventId, eventName, eventDescription, organizerId, eventCapacity, geolocationRequired, tvStartDate.getText().toString(), tvEndDate.getText().toString(), eventPosterUrl, qrUri, selectedFacility);

        // Save the event to Firestore
        db.collection("events").document(eventId).set(event.toMap())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateEventActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                    // Navigate to the QR code activity
                    Intent intent = new Intent(CreateEventActivity.this, SaveQRCodeActivity.class);
                    intent.putExtra("qrCodeByteArray", qrCode);     // Pass the byte array
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error creating event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Generate a QR code in byte array format
    private byte[] generateQRCodeForEvent(String eventId) {
        try {
            // Generate the QR code Bitmap
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(eventId, BarcodeFormat.QR_CODE, 200, 200);

            // Convert the Bitmap into a byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            return null;
        }
    }

    // Turn a Byte array into a base64-encoded Uri to be stored in Firestore
    public String byteArrayToUri(byte[] byteArray) {
        // Convert byte array to Base64 string
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}