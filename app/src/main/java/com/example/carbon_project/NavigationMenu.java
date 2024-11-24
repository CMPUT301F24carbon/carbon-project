package com.example.carbon_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class NavigationMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

//        // Setup Top Navigation
//        MaterialToolbar topNavigation = findViewById(R.id.top_navigation);
//        if (topNavigation != null) {
//            topNavigation.setOnMenuItemClickListener(this::onTopNavigationItemSelected);
//        }

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        if (bottomNavigation != null) {
            bottomNavigation.setOnNavigationItemSelectedListener(this::onBottomNavigationItemSelected);
        }
    }

    // Abstract method to enforce layouts in child activities
    protected abstract int getLayoutResourceId();

//    // Handle Top Navigation Menu Clicks
//    private boolean onTopNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_manage_accounts) {
//            // Navigate to account management
//            startActivity(new Intent(this, ManageAccountsActivity.class));
//            return true;
//        } else if (id == R.id.action_search) {
//            // Open search functionality
//            startActivity(new Intent(this, SearchActivity.class));
//            return true;
//        } else if (id == R.id.action_filter) {
//            // Open filter screen
//            startActivity(new Intent(this, FilterActivity.class));
//            return true;
//        } else if (id == R.id.action_notification) {
//            // Show notifications
//            startActivity(new Intent(this, NotificationsActivity.class));
//            return true;
//        }
//
//        return false;
//    }

    // Handle Bottom Navigation Menu Clicks
    private boolean onBottomNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_list) {
            // Navigate to the list screen
            startActivity(new Intent(this, EventListActivity.class));
            return true;
        } else if (id == R.id.navigation_add_circle) {
            // Open Add New Item screen
            startActivity(new Intent(this, CreateEventActivity.class));
            return true;
        } else if (id == R.id.navigation_person) {
            // Navigate to profile screen
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return false;
    }
}