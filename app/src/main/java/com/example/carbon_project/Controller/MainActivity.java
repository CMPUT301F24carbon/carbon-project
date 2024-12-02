package com.example.carbon_project.Controller;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlePermissions();

        //this allows the app to recieve notifications in the background
        NotificationChannel channel = new NotificationChannel(
                "eventChannel",
                "Event Notifications",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        // Listen for button clicks
        findViewById(R.id.entrant_button).setOnClickListener(view -> createUser("entrant"));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createUser("organizer"));
        findViewById(R.id.admin_button).setOnClickListener(view -> createUser("admin"));
    }

    private void createUser(String role) {
        // Navigate to the corresponding dashboard activity
        Intent intent;
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Load user data from Firestore
        FirebaseFirestore.getInstance().collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> dataMap;
                    if (documentSnapshot.exists()) {
                        dataMap = documentSnapshot.getData();
                        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If the document doesn't exist, create a new user
                        dataMap = new HashMap<>();
                        dataMap.put("userId", deviceId);
                        dataMap.put("role", role);
                        Toast.makeText(this, "Welcome new user!", Toast.LENGTH_SHORT).show();
                    }

                    // Create the appropriate user object based on the role
                    if (role.equals("entrant")) {
                        user = new Entrant(dataMap);
                        toActivity(EntrantDashboardActivity.class);
                    } else if (role.equals("organizer")) {
                        user = new Organizer(dataMap);
                        toActivity(OrganizerDashboardActivity.class);
                    } else {
                        user = new Admin(dataMap);
                        toActivity(AdminListActivity.class);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void toActivity(Class<?> activityClass) {
        Intent intent;
        intent = new Intent(MainActivity.this, activityClass);
        intent.putExtra("userObject", user);
        startActivity(intent);
    }

    private void handlePermissions() {
        // Check for both permissions
        boolean cameraGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean notificationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        if (cameraGranted && notificationGranted) {
            // Both permissions are already granted
            Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
        } else {
            // Show rationale if needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "Camera and notification permissions are required for this app", Toast.LENGTH_LONG).show();
            }
            // Request both permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.POST_NOTIFICATIONS
                    },
                    101);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            boolean cameraGranted = false;
            boolean notificationGranted = false;
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    cameraGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                } else if (permissions[i].equals(Manifest.permission.POST_NOTIFICATIONS)) {
                    notificationGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }
            if (cameraGranted && notificationGranted) {
                Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
            } else if (!cameraGranted) {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            } else if (!notificationGranted) {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
