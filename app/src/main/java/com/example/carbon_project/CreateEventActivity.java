package com.example.carbon_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * CreateEventActivity is an activity for creating new events. It provides
 * functionalities for inputting event details, selecting event dates, uploading
 * an event image, and validating inputs before creating and publishing the event.
 */
public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameInput;
    private EditText eventCapacityInput;
    private EditText eventStartDateInput;
    private EditText eventEndDateInput;
    private CheckBox geolocationCheckbox;
    private Button publishButton;
    private Button backButton;
    private Spinner eventLocationSpinner;
    private AppCompatImageView eventImageView;
    private String selectedFacilityName;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Initialize UI elements
        eventNameInput = findViewById(R.id.eventNameInput);
        eventCapacityInput = findViewById(R.id.eventCapacityInput);
        eventStartDateInput = findViewById(R.id.eventStartDateInput);
        eventEndDateInput = findViewById(R.id.eventEndDateInput);
        geolocationCheckbox = findViewById(R.id.eventCheckbox);
        publishButton = findViewById(R.id.publishButton);
        backButton = findViewById(R.id.backButton);
        eventLocationSpinner = findViewById(R.id.eventLocationSpinner);
        eventImageView = findViewById(R.id.eventImage);

        populateFacilities();

        // Set up date pickers for start and end date
        eventStartDateInput.setOnClickListener(v -> showDatePickerDialog(eventStartDateInput));
        eventEndDateInput.setOnClickListener(v -> showDatePickerDialog(eventEndDateInput));

        publishButton.setOnClickListener(v -> createEvent());
        backButton.setOnClickListener(v -> finish());

        // Click on camera icon to upload an image
        eventImageView.setOnClickListener(v -> openGallery());
    }

    /**
     * Populates the event location spinner with a list of facility names.
     * Sets up a listener to capture the selected facility.
     */
    private void populateFacilities() {
        ArrayList<Facility> facilities = FacilityManager.getInstance().getFacilities();
        ArrayList<String> facilityNames = new ArrayList<>();

        // Initialize test data if no facilities are available FOR TESTING
        if (facilities.isEmpty()) {
            Facility grandArena = new Facility("F001", "Grand Arena", "123 Main St, Downtown", 1000, "A Luxury Hotel");
            Facility downtownHall = new Facility("F002", "Downtown Hall", "456 Elm St, City Center", 500, "A Beautiful Penthouse overlooking NYC");
            facilities.add(grandArena);
            facilities.add(downtownHall);
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
     * Validates input fields and creates a new event with the specified details.
     * Displays a message if any fields are incomplete or invalid.
     */
    private void createEvent() {
        String eventName = eventNameInput.getText().toString().trim();
        String eventCapacityStr = eventCapacityInput.getText().toString().trim();
        String eventStartDate = eventStartDateInput.getText().toString().trim();
        String eventEndDate = eventEndDateInput.getText().toString().trim();

        // Validate input fields
        if (eventName.isEmpty() || eventCapacityStr.isEmpty() || eventStartDate.isEmpty() || eventEndDate.isEmpty() || selectedFacilityName == null) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that event capacity is a number
        int eventCapacity;
        try {
            eventCapacity = Integer.parseInt(eventCapacityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that the start date is before the end date
        if (!isStartDateBeforeEndDate(eventStartDate, eventEndDate)) {
            Toast.makeText(this, "Start date must be before end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean geolocationRequired = geolocationCheckbox.isChecked();
        String eventId = generateUniqueId();

        Event newEvent = new Event(eventId, eventName, eventStartDate, selectedFacilityName, eventCapacity, geolocationRequired, eventStartDate, eventEndDate);
        EventManager.getInstance().addEvent(newEvent);

        Intent resultIntent = new Intent(this, EventListActivity.class);
        startActivity(resultIntent);
        Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();

        clearInputs();
    }

    /**
     * Generates a unique identifier for a new event using UUID.
     * @return A unique event ID string.
     */
    private String generateUniqueId() {
        return "EVT" + UUID.randomUUID().toString();
    }

    /**
     * Checks if the start date is before the end date.
     * @param startDate The event start date in the format "yyyy-MM-dd".
     * @param endDate The event end date in the format "yyyy-MM-dd".
     * @return True if start date is before end date, false otherwise.
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
     * Shows a date picker dialog for selecting a date. The selected date
     * is set to the specified EditText field.
     * @param editText The EditText field where the selected date will be displayed.
     */
    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", getResources().getConfiguration().locale);
            calendar.set(selectedYear, selectedMonth, selectedDay);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Clears all input fields in the activity, resetting them to their default state.
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
     * Opens the gallery to allow the user to select an image for the event poster.
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Handles the result of the image selection from the gallery.
     * @param requestCode The request code for the intent.
     * @param resultCode The result code of the activity.
     * @param data The data returned by the intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
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
}