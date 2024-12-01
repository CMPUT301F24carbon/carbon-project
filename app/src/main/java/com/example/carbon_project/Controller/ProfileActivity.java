package com.example.carbon_project.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;

    private EditText name, email, phoneNumber;
    private ImageView profilePicture;
    private TextView initialsBadge;
    private Button uploadPictureButton, removePictureButton, saveProfileButton;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        name = findViewById(R.id.name_input);
        email = findViewById(R.id.email_input);
        phoneNumber = findViewById(R.id.phone_input);
        profilePicture = findViewById(R.id.profile_picture);
        initialsBadge = findViewById(R.id.initials_badge);
        uploadPictureButton = findViewById(R.id.upload_picture_button);
        removePictureButton = findViewById(R.id.remove_picture_button);
        saveProfileButton = findViewById(R.id.save_profile_button);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            // Fallback to device ID if no userId is provided
            userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        loadProfile(userId);

        // Set listeners for upload/remove/save
        uploadPictureButton.setOnClickListener(v -> uploadProfilePicture());
        removePictureButton.setOnClickListener(v -> removeProfilePicture());
        saveProfileButton.setOnClickListener(v -> saveProfile());

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

  /*  private void uploadProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("ProfileActivity", "Entrant User ID: " + entrant.getUserId());
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("profile_pictures/" + entrant.getUserId() + ".jpg");

            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String profilePictureUrl = uri.toString();
                        db.collection("users").document(entrant.getUserId())
                                .update("profilePictureUrl", profilePictureUrl)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
                                    profilePicture.setVisibility(View.VISIBLE);
                                    initialsBadge.setVisibility(View.GONE);
                                    Picasso.get().load(profilePictureUrl).into(profilePicture);
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }).addOnFailureListener(e -> Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show()))
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload picture: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void uploadProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("profile_pictures/" + userId + ".jpg");

            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String profilePictureUrl = uri.toString();
                                db.collection("users").document(userId)
                                        .update("profilePictureUrl", profilePictureUrl)
                                        .addOnSuccessListener(aVoid -> {
                                            profilePicture.setVisibility(View.VISIBLE);
                                            initialsBadge.setVisibility(View.GONE);
                                            Picasso.get().load(profilePictureUrl).into(profilePicture);
                                            Toast.makeText(this, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to update Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show()))
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload picture: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadProfile(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nameValue = documentSnapshot.getString("name");
                        String emailValue = documentSnapshot.getString("email");
                        String phoneValue = documentSnapshot.getString("phoneNumber");
                        String profilePictureUrl = documentSnapshot.getString("profilePictureUrl");

                        if (nameValue != null) {
                            name.setText(nameValue);
                            displayInitials(nameValue);
                        }
                        if (emailValue != null) {
                            email.setText(emailValue);
                        }
                        if (phoneValue != null) {
                            phoneNumber.setText(phoneValue);
                        }

                        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                            initialsBadge.setVisibility(View.GONE);
                            profilePicture.setVisibility(View.VISIBLE);
                            Picasso.get()
                                    .load(profilePictureUrl)
                                    .placeholder(R.drawable.placeholder_profile_image)
                                    .error(R.drawable.placeholder_profile_image)
                                    .into(profilePicture);
                        } else {
                            profilePicture.setVisibility(View.GONE);
                            initialsBadge.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(this, "User profile not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void saveProfile() {
        String nameValue = name.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        String phoneValue = phoneNumber.getText().toString().trim();


        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", nameValue);
        profileData.put("email", emailValue);
        profileData.put("phoneNumber", phoneValue);

        db.collection("users").document(userId)
                .set(profileData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    displayInitials(nameValue);
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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

    private void removeProfilePicture() {
        db.collection("users").document(userId)
                .update("profilePictureUrl", null)
                .addOnSuccessListener(aVoid -> {
                    profilePicture.setVisibility(View.GONE);
                    initialsBadge.setVisibility(View.VISIBLE);
                    displayInitials(name.getText().toString());
                    Toast.makeText(this, "Profile picture removed.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}