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

        //this checks and asks for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Ask for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1);
            }
        }

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

                    // Save the user data to Firestore
                    user.saveToFirestore();
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
}
