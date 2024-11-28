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

        User gagan = new User(deviceId, "Gagan Cheema", "Gagan.Cheema@example.com","123-456-789","Entrant");

        findViewById(R.id.entrant_button).setOnClickListener(view -> createEntrantUser(deviceId, gagan.getName(), gagan.getEmail(), gagan.getPhoneNumber()));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createOrganizerUser(deviceId, gagan.getName(), gagan.getEmail(), gagan.getPhoneNumber()));
        findViewById(R.id.admin_button).setOnClickListener(view -> createAdminUser(deviceId, gagan.getName(), gagan.getEmail(), gagan.getPhoneNumber()));
    }

    private void createOrganizerUser(String userId, String name, String email, String phoneNumber) {
        Organizer organizer = new Organizer(userId, name, email, phoneNumber);
        organizer.saveToFirestore();

        Intent intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
        intent.putExtra("organizerObject", organizer);
        startActivity(intent);
    }

    private void createAdminUser(String userId, String name, String email, String phoneNumber) {
        Admin admin = new Admin(userId, name, email, phoneNumber);
        admin.saveToFirestore();

        Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
        intent.putExtra("adminObject", admin);
        startActivity(intent);
    }

    private void createEntrantUser(String userId, String name, String email, String phoneNumber) {
        Entrant entrant = new Entrant(userId, name, email, phoneNumber);
        entrant.saveToFirestore();

        Intent intent = new Intent(MainActivity.this, EntrantDashboardActivity.class);
        intent.putExtra("entrantObject", entrant);
        startActivity(intent);
    }
}