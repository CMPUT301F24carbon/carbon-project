package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AdminViewProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView initialsBadge;
    private Button deleteProfile, deletePicture;
    private TextView profileName, profileEmail, profilePhone, profileRole;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_profile);

        User user = (User) getIntent().getSerializableExtra("userObject");

        if (user == null) {
            Toast.makeText(this, "Profile data is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        deletePicture = findViewById(R.id.delete_picture_button);
        deleteProfile = findViewById(R.id.delete_profile_button);

        profilePicture = findViewById(R.id.profile_picture);

        initialsBadge = findViewById(R.id.initials_badge);
        profileName = findViewById(R.id.name);
        profileEmail = findViewById(R.id.email);
        profilePhone = findViewById(R.id.phone);
        profileRole = findViewById(R.id.role);

        profileName.setText(user.getName());
        profileEmail.setText(user.getEmail());
        profilePhone.setText(user.getPhoneNumber());
        profileRole.setText(user.getRole());

        if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
            initialsBadge.setVisibility(View.GONE);
            profilePicture.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.placeholder_profile_image)
                    .error(R.drawable.placeholder_profile_image)
                    .into(profilePicture);
        } else {
            profilePicture.setVisibility(View.GONE);
            displayInitials(user.getName());
            initialsBadge.setVisibility(View.VISIBLE);
        }

        deletePicture.setOnClickListener(v -> {
            user.setProfilePictureUrl("");
            user.saveToFirestore();
            finish();
        });

        deleteProfile.setOnClickListener(v -> {
            db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUserId()).delete();
            finish();
        });
    }

    private void displayInitials(String name) {
        String initials = "";
        if (name != null && !name.isEmpty()) {
            String[] nameParts = name.trim().split("\\s+");
            if (nameParts.length > 0) {
                initials += nameParts[0].charAt(0);
            }
            if (nameParts.length > 1) {
                initials += nameParts[1].charAt(0);
            }
        }
        initialsBadge.setText(initials.toUpperCase());
    }
}