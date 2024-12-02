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

/**
 * The MainActivity class is the entry point of the application.
 */
public class MainActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 101;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //this checks and asks for permissions
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

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        User abdul = new User(deviceId, "YL", "pleasepass@example.com", "123-456-789", "Organizer");
        findViewById(R.id.entrant_button).setOnClickListener(view -> createUser(abdul, Entrant.class));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createUser(abdul, Organizer.class));
        findViewById(R.id.admin_button).setOnClickListener(view -> createUser(abdul, Admin.class));
    }


    /**
     * Handles the permissions required for the app.
     */
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
                    MULTIPLE_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     * @param requestCode The request code passed.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS_REQUEST_CODE) {
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
    /**
     * Create a new user and navigate to the corresponding dashboard activity.
     * @param user
     * @param userType
     */
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
