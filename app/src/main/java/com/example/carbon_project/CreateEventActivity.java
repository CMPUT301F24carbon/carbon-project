package com.example.carbon_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * CreateEventActivity is an activity for creating new events. It provides
 * functionalities for inputting event details, selecting event dates, uploading
 * an event image, and validating inputs before creating and publishing the event.
 */
public class CreateEventActivity extends NavigationMenu {

    private EditText eventNameInput;
    private EditText eventCapacityInput;
    private EditText eventStartDateInput;
    private EditText eventEndDateInput;
    private EditText eventDescriptionInput;
    private CheckBox geolocationCheckbox;
    private Button publishButton;
    private Button backButton;
    private Spinner eventLocationSpinner;
    private ImageView eventImageView;
    private String selectedFacilityName;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.create_event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // Initialize UI elements
        initializeUI();

        // Set up date pickers for start and end date inputs
        setupDatePickers();

        // Set up buttons and image view click listeners
        setupButtonListeners();
    }

    /**
     * Initializes UI elements.
     */
    private void initializeUI() {
        eventNameInput = findViewById(R.id.eventNameInput);
        eventCapacityInput = findViewById(R.id.eventCapacityInput);
        eventStartDateInput = findViewById(R.id.eventStartDateInput);
        eventEndDateInput = findViewById(R.id.eventEndDateInput);
        eventDescriptionInput = findViewById(R.id.eventDescriptionInput);
        geolocationCheckbox = findViewById(R.id.eventCheckbox);
        publishButton = findViewById(R.id.publishButton);
        backButton = findViewById(R.id.backButton);
        eventLocationSpinner = findViewById(R.id.eventLocationSpinner);
        eventImageView = findViewById(R.id.eventImage);

        populateFacilities();
    }

    /**
     * Populates the event location spinner with facility names.
     */
    private void populateFacilities() {
        ArrayList<Facility> facilities = FacilityManager.getInstance().getFacilities();
        ArrayList<String> facilityNames = new ArrayList<>();

        // Test data for facilities if none exist (for testing purposes)
        if (facilities.isEmpty()) {
            facilities.add(new Facility("Grand Arena", "123 Main St", 1000, "Luxury hotel"));
            facilities.add(new Facility("Downtown Hall", "456 Elm St", 500, "Penthouse in NYC"));
        }

        for (Facility facility : facilities) {
            facilityNames.add(facility.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventLocationSpinner.setAdapter(adapter);

        eventLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedFacilityName = facilityNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedFacilityName = null;
            }
        });
    }

    /**
     * Sets up date pickers for event date inputs.
     */
    private void setupDatePickers() {
        eventStartDateInput.setOnClickListener(v -> showDatePickerDialog(eventStartDateInput));
        eventEndDateInput.setOnClickListener(v -> showDatePickerDialog(eventEndDateInput));
    }

    /**
     * Sets up listeners for buttons and the event image view.
     */
    private void setupButtonListeners() {
        publishButton.setOnClickListener(v -> createEvent());
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Validates inputs and creates an event if valid.
     */
    private void createEvent() {
        String eventName = eventNameInput.getText().toString().trim();
        String eventCapacityStr = eventCapacityInput.getText().toString().trim();
        String eventStartDate = eventStartDateInput.getText().toString().trim();
        String eventEndDate = eventEndDateInput.getText().toString().trim();
        String eventDescription = eventDescriptionInput.getText().toString().trim();

        // Input validation
        if (!validateInputs(eventName, eventCapacityStr, eventStartDate, eventEndDate)) {
            return;
        }

        int eventCapacity = Integer.parseInt(eventCapacityStr);
        boolean geolocationRequired = geolocationCheckbox.isChecked();

        Event newEvent = new Event(eventName, selectedFacilityName, eventCapacity, geolocationRequired, eventStartDate, eventEndDate, eventDescription, 0, null);
        String userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        newEvent.setUserId(userId);
        // Use the uploadToFirestore method from Event class to upload the event data
        newEvent.uploadToFirestore();


        // After uploading, navigate to EventListActivity
        Intent resultIntent = new Intent(this, EventListActivity.class);
        clearInputs();
        startActivity(resultIntent);
        Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Validates that required inputs are filled and correct.
     */
    private boolean validateInputs(String eventName, String eventCapacityStr, String eventStartDate, String eventEndDate) {
        if (eventName.isEmpty() || eventCapacityStr.isEmpty() || eventStartDate.isEmpty() || eventEndDate.isEmpty() || selectedFacilityName == null) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Integer.parseInt(eventCapacityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity must be a number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isStartDateBeforeEndDate(eventStartDate, eventEndDate)) {
            Toast.makeText(this, "Start date must be before end date.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Checks if start date is before end date.
     */
    private boolean isStartDateBeforeEndDate(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", getResources().getConfiguration().locale);
        try {
            return dateFormat.parse(startDate).before(dateFormat.parse(endDate));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Displays a date picker dialog.
     */
    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(selectedYear, selectedMonth, selectedDay);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", getResources().getConfiguration().locale);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Clears input fields.
     */
    private void clearInputs() {
        eventNameInput.setText("");
        eventCapacityInput.setText("");
        eventStartDateInput.setText("");
        eventEndDateInput.setText("");
        eventLocationSpinner.setSelection(0);
        geolocationCheckbox.setChecked(false);
    }

    /**
     * Opens the gallery to select an image.
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Handles image selection from the gallery.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                eventImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}