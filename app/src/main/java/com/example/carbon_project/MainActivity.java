package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Example User IDs for testing
        String entrantId = "1";
        String adminId = "0";

        // Creating users for demonstration
        createOrganizerUser(deviceId, "Gagan Cheema", "gcheema@example.com", "123-456-789");
        //createAdminUser(adminId, "Admin User", "admin@example.com", "987-654-321");
        //createEntrantUser(entrantId, "Entrant User", "entrant@example.com", "111-222-333");
    }

    private void createOrganizerUser(String userId, String name, String email, String phoneNumber) {
        Organizer organizer = new Organizer(userId, name, email, phoneNumber);
        organizer.saveToFirestore();

        // Pass the Entrant object to the next activity
        Intent intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
        intent.putExtra("organizerObject", organizer);
        startActivity(intent);
    }

    private void createAdminUser(String userId, String name, String email, String phoneNumber) {
        Admin admin = new Admin(userId, name, email, phoneNumber);
        admin.saveToFirestore();
        goToAdminView();
    }

    private void createEntrantUser(String userId, String name, String email, String phoneNumber) {
        Entrant entrant = new Entrant(userId, name, email, phoneNumber);
        entrant.saveToFirestore();

        // Pass the Entrant object to the next activity
        Intent intent = new Intent(MainActivity.this, EntrantDashboardActivity.class);
        intent.putExtra("entrantObject", entrant);
        startActivity(intent);
    }


    private void goToOrganizerView() {
        startActivity(new Intent(MainActivity.this, OrganizerDashboardActivity.class));
    }

    private void goToAdminView() {
        startActivity(new Intent(MainActivity.this, AdminDashboardActivity.class));
    }

    private void goToEntrantView() {
        startActivity(new Intent(MainActivity.this, EntrantDashboardActivity.class));
    }

}