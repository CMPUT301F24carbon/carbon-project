package com.example.carbon_project.Controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * The CreateEventActivity class is an activity that allows the user to create a new event.
 */
public class CreateEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription, etEventCapacity;
    private TextView tvStartDate, tvEndDate;
    private Button btnSaveEvent;
    private CheckBox cbGeolocationRequired;
    private Spinner spFacility;
    private ImageView ivEventPoster;
    private FirebaseFirestore db;
    private String organizerId;
    private ArrayList<Facility> facilityList;
    private Uri eventPosterUri;

    // Variables for the date picker
    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;

    private static final int PICK_IMAGE_REQUEST = 1;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();
        organizerId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize views
        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventCapacity = findViewById(R.id.etEventCapacity);  // Capacity is now optional
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        cbGeolocationRequired = findViewById(R.id.cbGeolocationRequired);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);
        spFacility = findViewById(R.id.spinnerFacility);
        ivEventPoster = findViewById(R.id.ivEventPoster);

        // Initialize the facility list
        facilityList = new ArrayList<>();

        tvStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        tvEndDate.setOnClickListener(v -> showDatePickerDialog(false));

        btnSaveEvent.setOnClickListener(v -> createAndSaveEvent());

        ivEventPoster.setOnClickListener(v -> openImagePicker());

        fetchFacilitiesForOrganizer(organizerId);

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

    /**
     * Handles the back button click event.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the date picker dialog.
     * @param isStartDate
     */
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

    /**
     * Format Date
     * @param year
     * @param month
     * @param day
     * @return
     */
    private String formatDate(int year, int month, int day) {
        // Format the date as YYYY-MM-DD
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Fetch facilities for organizer
     * @param organizerId
     */
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

    /**
     * Populate the spinner with facility names.
     */
    private void populateFacilitySpinner() {
        ArrayList<String> facilityNames = new ArrayList<>();
        for (Facility facility : facilityList) {
            facilityNames.add(facility.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFacility.setAdapter(adapter);
    }

    /**
     * Create and save event
     */
    private void createAndSaveEvent() {
        // Validate inputs
        String eventName = etEventName.getText().toString().trim();
        String eventDescription = etEventDescription.getText().toString().trim();

        // If eventCapacity is empty, set it to -1 (optional field)
        int eventCapacity = -1; // Default value when the field is empty
        String capacityText = etEventCapacity.getText().toString().trim();
        if (!capacityText.isEmpty()) {
            try {
                eventCapacity = Integer.parseInt(capacityText);
            } catch (NumberFormatException e) {
                etEventCapacity.setError("Invalid capacity");
                return;
            }
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

        // Set default event poster URL
        String eventPosterUrl = "default_poster_url";

        // Create the Event object with placeholder poster URL
        Event event = new Event(eventId, eventName, eventDescription, organizerId, eventCapacity, geolocationRequired, tvStartDate.getText().toString(), tvEndDate.getText().toString(), eventPosterUrl, qrUri, selectedFacility);

        if (eventPosterUri != null) {
            // Upload the event poster
            uploadEventPoster(eventId, event, qrCode);
        } else {
            // If no poster, directly save event to Firestore
            saveEventToFirestore(event, qrCode);
        }
    }

    /**
     * Upload event poster
     * @param eventId
     * @param event
     * @param qrCode
     */
    private void uploadEventPoster(String eventId, final Event event, byte[] qrCode) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference eventPosterRef = storageReference.child("event_posters/" + eventId + ".jpg");

        // Upload image
        eventPosterRef.putFile(eventPosterUri)
                .addOnSuccessListener(taskSnapshot -> {
                    eventPosterRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String eventPosterUrl = uri.toString();
                        event.setEventPosterUrl(eventPosterUrl); // Update the event's poster URL
                        Toast.makeText(CreateEventActivity.this, "Poster uploaded successfully", Toast.LENGTH_SHORT).show();

                        // After poster upload, save the event to Firestore
                        saveEventToFirestore(event, qrCode);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error uploading poster: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Save event to Firestore
     * @param event
     * @param qrCode
     */
    private void saveEventToFirestore(Event event, byte[] qrCode) {
        db.collection("events").document(event.getEventId()).set(event.toMap())
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

    /**
     * Generate QR code
     * @param eventId
     * @return
     */
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

    /**
     * Convert byte array to URI
     * @param byteArray
     * @return
     */
    private String byteArrayToUri(byte[] byteArray) {
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    /**
     * Upload event poster
     * @param eventId
     * @param event
     */
    private void uploadEventPoster(String eventId, final Event event) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference eventPosterRef = storageReference.child("event_posters/" + eventId + ".jpg");

        // Upload image
        eventPosterRef.putFile(eventPosterUri)
                .addOnSuccessListener(taskSnapshot -> {
                    eventPosterRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String eventPosterUrl = uri.toString();
                        event.setEventPosterUrl(eventPosterUrl); // Update the event's poster URL
                        Toast.makeText(CreateEventActivity.this, "Poster uploaded successfully", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error uploading poster: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Open image picker
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Event Poster"), PICK_IMAGE_REQUEST);
    }

    /**
     * Handle the result of the image picker
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            eventPosterUri = data.getData();
            ivEventPoster.setImageURI(eventPosterUri);
        }
    }
}
