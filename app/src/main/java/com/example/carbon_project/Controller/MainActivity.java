package com.example.carbon_project.Controller;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;

public class MainActivity extends AppCompatActivity {

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

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        User abdul = new User(deviceId, "YL", "pleasepass@example.com", "123-456-789", "Organizer");
        findViewById(R.id.entrant_button).setOnClickListener(view -> createUser(abdul, Entrant.class));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createUser(abdul, Organizer.class));
        findViewById(R.id.admin_button).setOnClickListener(view -> createUser(abdul, Admin.class));
    }

    private void createUser(User user, Class<? extends User> userType) {
        User userInstance;

        if (userType == Entrant.class) {
            userInstance = new Entrant(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
        } else if (userType == Organizer.class) {
            userInstance = new Organizer(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
        } else if (userType == Admin.class) {
            userInstance = new Admin(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        // Save user to Firestore (or any other database)
        userInstance.saveToFirestore();

        // Navigate to the corresponding dashboard activity
        Intent intent;
        if (userInstance instanceof Entrant) {
            intent = new Intent(MainActivity.this, EntrantDashboardActivity.class);
            intent.putExtra("userObject", (Entrant) userInstance);
        } else if (userInstance instanceof Organizer) {
            intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
            intent.putExtra("userObject", (Organizer) userInstance);
        } else if (userInstance instanceof Admin) {
            intent = new Intent(MainActivity.this, AdminListActivity.class);
            intent.putExtra("userObject", (Admin) userInstance);
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        startActivity(intent);
    }
}
