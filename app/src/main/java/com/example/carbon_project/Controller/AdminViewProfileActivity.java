package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminViewProfileActivity extends AppCompatActivity {

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

        profileName = findViewById(R.id.name);
        profileEmail = findViewById(R.id.email);
        profilePhone = findViewById(R.id.phone);
        profileRole = findViewById(R.id.role);

        profileName.setText(user.getName());
        profileEmail.setText(user.getEmail());
        profilePhone.setText(user.getPhoneNumber());
        profileRole.setText(user.getRole());

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
}