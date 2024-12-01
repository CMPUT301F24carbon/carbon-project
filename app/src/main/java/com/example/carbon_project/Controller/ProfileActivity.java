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

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;

    private EditText name, email, phoneNumber;
    private ImageView profilePicture;
    private TextView initialsBadge;
    private Button uploadPictureButton, removePictureButton, saveProfileButton;
    private FirebaseFirestore db;
    private User activeType;

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

        // Get the user object from the Intent
        User user = (User) getIntent().getSerializableExtra("userObject");

        if (user != null) {
            if (user instanceof Entrant) {
                Entrant entrant = (Entrant) user;
                loadProfile(entrant.getUserId());
                activeType = entrant;
            }
            else if (user instanceof Organizer) {
                Organizer organizer = (Organizer) user;
                loadProfile(organizer.getUserId());
                activeType = organizer;
            }
            else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                loadProfile(admin.getUserId());
                activeType = admin;
            } else {
                Toast.makeText(this, "Unknown user type!", Toast.LENGTH_SHORT).show();
            }
        } else {
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            loadProfile(deviceId);
        }


        // Set listeners for upload/remove/save
        uploadPictureButton.setOnClickListener(v -> uploadProfilePicture());
        removePictureButton.setOnClickListener(v -> removeProfilePicture(activeType));
        saveProfileButton.setOnClickListener(v -> saveProfile(activeType));

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

            Log.d("ProfileActivity", "Selected Image URI: " + selectedImageUri);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("profile_pictures/" + activeType.getUserId() + ".jpg");

            // Ensure valid userId
            if (activeType.getUserId() == null) {
                Toast.makeText(this, "User ID is null. Cannot upload picture.", Toast.LENGTH_SHORT).show();
                return;
            }

            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("ProfileActivity", "File uploaded successfully.");
                        storageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String profilePictureUrl = uri.toString();
                                    db.collection("users").document(activeType.getUserId())
                                            .update("profilePictureUrl", profilePictureUrl)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
                                                profilePicture.setVisibility(View.VISIBLE);
                                                initialsBadge.setVisibility(View.GONE);
                                                Picasso.get().load(profilePictureUrl).into(profilePicture);
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to update Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload picture: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }




    private void saveProfile(User userType) {
        userType.setName(name.getText().toString().trim());
        userType.setEmail(email.getText().toString().trim());
        userType.setPhoneNumber(phoneNumber.getText().toString().trim());
        displayInitials(userType.getName());
        db.collection("users")
                .document(userType.getUserId())
                .set(userType)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show())
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

    private void removeProfilePicture(User userType) {
        userType.setProfilePictureUrl(null);
        db.collection("users")
                .document(userType.getUserId())
                .update("profilePictureUrl", null)
                .addOnSuccessListener(aVoid -> {
                    profilePicture.setVisibility(View.GONE);
                    initialsBadge.setVisibility(View.VISIBLE);
                    displayInitials(userType.getName());
                    Toast.makeText(this, "Profile picture removed.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}