package com.example.carbon_project.Controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

public class CreateEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription, etEventCapacity;
    private TextView tvStartDate, tvEndDate;
    private Button btnSaveEvent;
    private CheckBox cbGeolocationRequired;
    private Spinner spFacility;
    private FirebaseFirestore db;
    private String organizerId;
    private ArrayList<Facility> facilityList;
    private ImageView ivEventPoster;
    private Uri selectedPosterUri;

    // Variables for the date picker
    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();
        organizerId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        // Initialize views
        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventCapacity = findViewById(R.id.etEventCapacity);  // Capacity is optional
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        cbGeolocationRequired = findViewById(R.id.cbGeolocationRequired);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);
        spFacility = findViewById(R.id.spinnerFacility);
        ivEventPoster = findViewById(R.id.ivEventPoster);

        // Initialize the facility list
        facilityList = new ArrayList<>();

        // Set listeners
        tvStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        tvEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        ivEventPoster.setOnClickListener(v -> openGalleryForImage());

        btnSaveEvent.setOnClickListener(v -> createAndSaveEvent());

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void fetchFacilitiesForOrganizer(String organizerId) {
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
        String eventName = etEventName.getText().toString().trim();
        String eventDescription = etEventDescription.getText().toString().trim();

        int eventCapacity = Integer.MAX_VALUE;
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

        boolean geolocationRequired = cbGeolocationRequired.isChecked();

        int selectedFacilityPosition = spFacility.getSelectedItemPosition();
        if (selectedFacilityPosition == -1) {
            Toast.makeText(this, "Please select a facility", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventId = UUID.randomUUID().toString();
        byte[] qrCode = generateQRCodeForEvent(eventId);
        String qrUri = byteArrayToUri(qrCode);

        if (selectedPosterUri != null) {
            uploadEventPoster(selectedPosterUri, eventId, qrCode, qrUri);
        } else {
            String eventPosterUrl = "default_poster_url";
            Event event = new Event(eventId, eventName, eventDescription, organizerId, eventCapacity, geolocationRequired, tvStartDate.getText().toString(), tvEndDate.getText().toString(), eventPosterUrl, qrUri);

            saveEventToFirestore(event);
        }
    }

    private void uploadEventPoster(Uri posterUri, String eventId, byte[] qrCode, String qrUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("event_posters/" + eventId + ".jpg");

        storageRef.putFile(posterUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String eventPosterUrl = uri.toString();
                        Event event = new Event(eventId, etEventName.getText().toString().trim(), etEventDescription.getText().toString().trim(), organizerId,
                                Integer.parseInt(etEventCapacity.getText().toString()), cbGeolocationRequired.isChecked(), tvStartDate.getText().toString(),
                                tvEndDate.getText().toString(), eventPosterUrl, qrUri);

                        saveEventToFirestore(event);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error uploading event poster: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveEventToFirestore(Event event) {
        db.collection("events").document(event.getEventId()).set(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateEventActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateEventActivity.this, SaveQRCodeActivity.class);
                    intent.putExtra("qrCodeByteArray", generateQRCodeForEvent(event.getEventId()));  // Pass the byte array
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateEventActivity.this, "Error saving event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private byte[] generateQRCodeForEvent(String eventId) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(eventId, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (WriterException e) {
            Log.e("CreateEvent", "Error generating QR code", e);
            return null;
        }
    }

    private String byteArrayToUri(byte[] qrCode) {
        return Base64.encodeToString(qrCode, Base64.DEFAULT);
    }

    private void openGalleryForImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100); // Request code 100
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedPosterUri = data.getData();
            ivEventPoster.setImageURI(selectedPosterUri);
        }
    }
}
