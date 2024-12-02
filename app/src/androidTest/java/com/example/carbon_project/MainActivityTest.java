package com.example.carbon_project;

package com.example.carbon_project;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.carbon_project.Controller.MainActivity;
import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Organizer;
import com.example.carbon_project.Model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    private String deviceId = Settings.Secure.getString(activityRule.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

    @Before
    public void setUp() {
        // Launch the activity before each test
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void testPermissionsGranted() {
        // Test that permissions are handled properly
        onView(ViewMatchers.withId(R.id.entrant_button)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.organizer_button)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.admin_button)).check(matches(isDisplayed()));

        // Verify that permission messages are shown (this is tricky in a test, so you'd likely mock the permissions part)
        // Normally, Espresso cannot simulate granting or denying permissions, but the app should behave as expected
    }

    @Test
    public void testEntrantButtonClick() {
        // Click on the Entrant button
        onView(withId(R.id.entrant_button)).perform(click());

        // Verify that the correct activity is launched (EntrantDashboardActivity)
        // You can check the existence of a view that should be present in the new activity
        onView(withId(R.id.someEntrantView)).check(matches(isDisplayed()));  // Replace with a valid view ID from EntrantDashboardActivity
    }

    @Test
    public void testOrganizerButtonClick() {
        // Click on the Organizer button
        onView(withId(R.id.organizer_button)).perform(click());

        // Verify that the correct activity is launched (OrganizerDashboardActivity)
        onView(withId(R.id.someOrganizerView)).check(matches(isDisplayed()));  // Replace with a valid view ID from OrganizerDashboardActivity
    }

    @Test
    public void testAdminButtonClick() {
        // Click on the Admin button
        onView(withId(R.id.admin_button)).perform(click());

        // Verify that the correct activity is launched (AdminListActivity)
        onView(withId(R.id.someAdminView)).check(matches(isDisplayed()));  // Replace with a valid view ID from AdminListActivity
    }

    @Test
    public void testCreateNewUserAndNavigate() {
        // Simulate creating a new user (we assume the user doesn't exist in Firestore)
        // This test assumes Firestore is mocked or can be simulated for testing purposes
        FirebaseFirestore.getInstance().collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    // Simulate a user creation flow
                    onView(withId(R.id.organizer_button)).perform(click());

                    // Check if user is created and the appropriate activity is navigated to
                    onView(withId(R.id.someOrganizerView)).check(matches(isDisplayed()));
                });
    }

    @Test
    public void testUserAlreadyExistsAndNavigate() {
        // Simulate a scenario where the user already exists in Firestore
        FirebaseFirestore.getInstance().collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Simulate an existing user (entrants in this example)
                        onView(withId(R.id.entrant_button)).perform(click());

                        // Check if the correct activity is navigated to
                        onView(withId(R.id.someEntrantView)).check(matches(isDisplayed()));
                    } else {
                        // If no user exists, create a new user
                        onView(withId(R.id.organizer_button)).perform(click());
                        onView(withId(R.id.someOrganizerView)).check(matches(isDisplayed()));
                    }
                });
    }

    @Test
    public void testPermissionDenied() {
        // Simulate permission denial scenario. This can be tricky in a test environment but usually,
        // you'd mock the permissions in the app or simulate the flow manually.

        // Click on the Entrant button
        onView(withId(R.id.entrant_button)).perform(click());

        // Verify the behavior when permissions are denied (for example, an error message might show)
        onView(withId(R.id.someErrorView)).check(matches(isDisplayed()));  // Replace with actual error UI component
    }
}
