/*
package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

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
}*/
package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;

/*

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        User gagan = new User(deviceId, "Gagan Cheema", "Gagan.Cheema@example.com","123-456-789","Entrant");

       */
/* findViewById(R.id.entrant_button).setOnClickListener(view -> createEntrantUser(deviceId, gagan.getName(), gagan.getEmail(), gagan.getPhoneNumber()));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createOrganizerUser(deviceId, gagan.getName(), gagan.getEmail(), gagan.getPhoneNumber()));
        findViewById(R.id.admin_button).setOnClickListener(view -> createAdminUser(deviceId, gagan.getName(), gagan.getEmail(), gagan.getPhoneNumber()));*//*


        findViewById(R.id.entrant_button).setOnClickListener(view -> createUser(gagan, Entrant.class));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createUser(gagan, Organizer.class));
        findViewById(R.id.admin_button).setOnClickListener(view -> createUser(gagan, Admin.class));

    }

    */
/*private void createOrganizerUser(String userId, String name, String email, String phoneNumber) {
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
    }*//*


 */
/*private void createUser(User user, Class<? extends User> userType) {
        try {
            User userInstance = userType.getConstructor(String.class, String.class, String.class, String.class)
                    .newInstance(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());

            userInstance.saveToFirestore();

            Intent intent;
            if (userInstance instanceof Entrant) {
                intent = new Intent(MainActivity.this, EntrantDashboardActivity.class);
            } else if (userInstance instanceof Organizer) {
                intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
            } else if (userInstance instanceof Admin) {
                intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
            } else {
                throw new IllegalArgumentException("Unknown user type");
            }

            intent.putExtra("userObject", userInstance);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//*

}*/
/*
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        User gagan = new User(deviceId, "Gagan Cheema", "Gagan.Cheema@example.com", "123-456-789","Admin");

        findViewById(R.id.entrant_button).setOnClickListener(view -> createUser(gagan, Entrant.class));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createUser(gagan, Organizer.class));
        findViewById(R.id.admin_button).setOnClickListener(view -> createUser(gagan, Admin.class));
    }

    private void createUser(User user, Class<? extends User> userType) {
        User userInstance;

        // Instantiate the user class based on the passed type
        if (userType == Entrant.class) {
            userInstance = new Entrant(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
        } else if (userType == Organizer.class) {
            userInstance = new Organizer(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
        } else if (userType == Admin.class) {
            userInstance = new Admin(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        // Save user to Firestore
        userInstance.saveToFirestore();

        // Navigate to the corresponding dashboard activity
        Intent intent;
        if (userInstance instanceof Entrant) {
            intent = new Intent(MainActivity.this, EntrantDashboardActivity.class);
            intent.putExtra("entrantObject", userInstance);
        } else if (userInstance instanceof Organizer) {
            intent = new Intent(MainActivity.this, OrganizerDashboardActivity.class);
            intent.putExtra("organizerObject", userInstance);
        } else if (userInstance instanceof Admin) {
            intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
            intent.putExtra("adminObject", userInstance);
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        startActivity(intent);
    }
}
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        User gagan = new User(deviceId, "Gagan Cheema", "Gagan.Cheema@example.com", "123-456-789", "Entrant");

        findViewById(R.id.entrant_button).setOnClickListener(view -> createUser(gagan, Entrant.class));
        findViewById(R.id.organizer_button).setOnClickListener(view -> createUser(gagan, Organizer.class));
        findViewById(R.id.admin_button).setOnClickListener(view -> createUser(gagan, Admin.class));
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
            intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
            intent.putExtra("userObject", (Admin) userInstance);
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        startActivity(intent);
    }
}
