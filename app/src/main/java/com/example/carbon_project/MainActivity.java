package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    User user;
    //Event event;
    Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_screen);

        // Apply the device's android ID as the user ID
        String userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        user = new User(userId);
        //event = new Event("Workshop", "Community Center", 50, false, "2023-11-10", "2023-11-11","", 0, null);
        facility = new Facility("Grand Arena", "123 Main St, Downtown",1000,"A Luxary Hotel");

        // Organizer
//        String facilityId = facility.getFacilityId();
//        User organizer = User.createUser(userId, "Organizer");
//        organizer.setFirstName("Gagan");
//        organizer.setLastName("Cheema");
//        organizer.setEmail("Gagan.Cheema@example.com");
//        organizer.setPhoneNumber("123-456-7890");
//        organizer.becomeOrganizer(facilityId);
//        organizer.uploadToFirestore();

        String facilityId = facility.getFacilityId();
        User organizer = User.createUser(userId, "Admin");
        organizer.setFirstName("Akash");
        organizer.setLastName("Brar");
        organizer.setEmail("Akash.Brar@example.com");
        organizer.setPhoneNumber("123-456-7890");
        organizer.becomeOrganizer(facilityId);
        organizer.uploadToFirestore();


        user.loadFromFirestore(new User.DataLoadedCallback() {
            @Override
            public void onDataLoaded(HashMap<String, Object> userData) {
                // Display the UI after the data is loaded
                setUpScreen();
                //user.uploadToFirestore();
                //event.uploadToFirestore();
                //facility.uploadToFirestore();
            }

            @Override
            public void onError(String error) {
                // Handle the error if the load fails
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_person);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_event_list) {
                    startActivity(new Intent(getApplicationContext(), EventListActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_add_circle) {
                    startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (item.getItemId() == R.id.navigation_person) {
                    return true;
                }
                if (item.getItemId() == R.id.navigation_facility_list) {
                    startActivity(new Intent(getApplicationContext(), FacilityListActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

    }

    public void setUpScreen() {
        // References to screen elements
        TextView userNameTextView = findViewById(R.id.userName);
        TextView userEmailTextView = findViewById(R.id.userEmail);
        TextView userPhoneTextView = findViewById(R.id.userPhone);
        ImageView profileImageView = findViewById(R.id.profileImageView);
        TextView initialsTextView = findViewById(R.id.initialsTextView);
        Button createFacilityButton = findViewById(R.id.createFacilityButton);
        Button createEventButton = findViewById(R.id.createEventButton);
        Button deleteUserButton = findViewById(R.id.deleteUserButton);

        // user data on user_screen
        userNameTextView.setText(user.getFullName());
        userEmailTextView.setText(user.getEmail());
        userPhoneTextView.setText(user.getPhoneNumber());

        // set profile image if available; otherwise, use initials
        if (user.getProfileImageUri() == null || user.getProfileImageUri().isEmpty()) {
            String initials = user.getInitials();
            initialsTextView.setText(initials);
            initialsTextView.setVisibility(TextView.VISIBLE);
            //profileImageView.setImageBitmap(event.getQRCodeBitmap());
        } else {
            initialsTextView.setVisibility(TextView.GONE);
            int imageResource = getResources().getIdentifier(user.getProfileImageUri(), "drawable", getPackageName());
            profileImageView.setImageResource(imageResource);
        }

        // Handle role-based actions
        if (user.getRole().toLowerCase(Locale.ROOT).equals("organizer")) {
            createFacilityButton.setVisibility(Button.VISIBLE);
            createEventButton.setVisibility(Button.VISIBLE); // Show Create Event button for Organizer

            createFacilityButton.setOnClickListener(v -> {
                // Start the create facility activity
                Intent intent = new Intent(MainActivity.this, CreateFacilityActivity.class);
                startActivity(intent);
            });

            createEventButton.setOnClickListener(v -> {
                // Start the create event activity
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
            });
        } else {
            createFacilityButton.setVisibility(Button.GONE);
            createEventButton.setVisibility(Button.GONE); // Hide Create Event button for non-Organizers
        }

        if (user.getRole().toLowerCase(Locale.ROOT).equals("admin")) {
            deleteUserButton.setVisibility(Button.VISIBLE);
        } else {
            deleteUserButton.setVisibility(Button.GONE);
        }
    }
}