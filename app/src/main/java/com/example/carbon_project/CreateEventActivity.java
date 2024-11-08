package com.example.carbon_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameInput;
    private EditText eventCapacityInput;
    private EditText eventStartDateInput;
    private EditText eventEndDateInput;
    private CheckBox geolocationCheckbox;
    private Button publishButton;
    private Button backButton;
    private Spinner eventLocationSpinner;
    private String selectedFacilityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        eventNameInput = findViewById(R.id.eventNameInput);
        eventCapacityInput = findViewById(R.id.eventCapacityInput);
        eventStartDateInput = findViewById(R.id.eventStartDateInput);
        eventEndDateInput = findViewById(R.id.eventEndDateInput);
        geolocationCheckbox = findViewById(R.id.eventCheckbox);
        publishButton = findViewById(R.id.publishButton);
        backButton = findViewById(R.id.backButton);
        eventLocationSpinner = findViewById(R.id.eventLocationSpinner);

        // Populate the Spinner with facilities
        populateFacilities();

        eventStartDateInput.setOnClickListener(v -> showDatePickerDialog(eventStartDateInput));
        eventEndDateInput.setOnClickListener(v -> showDatePickerDialog(eventEndDateInput));

        publishButton.setOnClickListener(v -> createEvent());

        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void populateFacilities() {
        ArrayList<Facility> facilities = FacilityManager.getInstance().getFacilities();
        ArrayList<String> facilityNames = new ArrayList<>();

        // TEST DATA
        Facility grandArena = new Facility("F001", "Grand Arena", "123 Main St, Downtown",1000,"A Luxary Hotel");
        Facility downtownHall = new Facility("F002", "Downtown Hall", "456 Elm St, City Center", 500 , "A Beautiful Penthouse overlooking the NYC");
        facilities.add(grandArena);
        facilities.add(downtownHall);


        // Populate the list with facility names
        for (Facility facility : facilities) {
            facilityNames.add(facility.getName());
        }

        // set spinner
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

    private void createEvent() {
        String eventName = eventNameInput.getText().toString().trim();
        String eventCapacityStr = eventCapacityInput.getText().toString().trim();
        String eventStartDate = eventStartDateInput.getText().toString().trim();
        String eventEndDate = eventEndDateInput.getText().toString().trim();

        if (eventName.isEmpty() || eventCapacityStr.isEmpty() || eventStartDate.isEmpty() || eventEndDate.isEmpty() || selectedFacilityName == null) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert capacity to integer
        int eventCapacity;
        try {
            eventCapacity = Integer.parseInt(eventCapacityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean geolocationRequired = geolocationCheckbox.isChecked();

        // Generate a unique event ID
        String eventId = "EVT" + System.currentTimeMillis();

        Event newEvent = new Event(eventId, eventName, eventStartDate, selectedFacilityName, eventCapacity, geolocationRequired);
        EventManager.getInstance().addEvent(newEvent);

        Intent resultIntent = new Intent(this, EventListActivity.class);
        startActivity(resultIntent);

        Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

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

    private void clearInputs() {
        eventNameInput.setText("");
        eventCapacityInput.setText("");
        eventStartDateInput.setText("");
        eventEndDateInput.setText("");
        eventLocationSpinner.setSelection(0);
        geolocationCheckbox.setChecked(false);
    }
}