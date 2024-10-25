package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_screen);

        // Test data for user organizer
        User organizer = new Organizer("O001", "Gagan","Cheema", "gagan.cheema@example.com", "123-456-7890", null);

        // To keep track of which type of user is active.
        User activeUser = organizer;

        // References to screen elements
        TextView userNameTextView = findViewById(R.id.userName);
        TextView userEmailTextView = findViewById(R.id.userEmail);
        TextView userPhoneTextView = findViewById(R.id.userPhone);
        ImageView profileImageView = findViewById(R.id.profileImageView);
        TextView initialsTextView = findViewById(R.id.initialsTextView); // New TextView for initials
        Button createFacilityButton = findViewById(R.id.createFacilityButton);
        Button deleteUserButton = findViewById(R.id.deleteUserButton);

        // user data on user_screen
        userNameTextView.setText(activeUser.getFullName());
        userEmailTextView.setText(activeUser.getEmail());
        userPhoneTextView.setText(activeUser.getPhoneNumber());

        // set profile image if available; otherwise, use initials
        if (activeUser.getProfileImageUrl() == null || activeUser.getProfileImageUrl().isEmpty()) {
            String initials = activeUser.getInitials();
            initialsTextView.setText(initials);
            initialsTextView.setVisibility(TextView.VISIBLE);
            profileImageView.setImageResource(R.drawable.profile_placeholder);
        } else {
            initialsTextView.setVisibility(TextView.GONE);
            int imageResource = getResources().getIdentifier(activeUser.getProfileImageUrl(), "drawable", getPackageName());
            profileImageView.setImageResource(imageResource);
        }

        // Handle role-based actions
        if (activeUser instanceof Organizer) {
            createFacilityButton.setVisibility(Button.VISIBLE);
            createFacilityButton.setOnClickListener(v -> {
                // Start the create facility activity
                Intent intent = new Intent(MainActivity.this, CreateFacilityActivity.class);
                startActivity(intent);
            });
        } else {
            createFacilityButton.setVisibility(Button.GONE);
        }

        if (activeUser instanceof Admin) {
            deleteUserButton.setVisibility(Button.VISIBLE);
        } else {
            deleteUserButton.setVisibility(Button.GONE);
        }
    }
}