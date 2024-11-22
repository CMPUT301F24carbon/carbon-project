package com.example.carbon_project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

// The methods you should know how to use are:
// 0. User(userId)          You only need to pass in the userId i.e. device ID
// 1. loadFromFirestore     Load user data from Firestore and update the instance variables
// 2. uploadToFirestore     Upload the instance variables to Firestore
// 3. handleNotification    Handle notification settings, store it in a hashmap
// 4. getFullName           Get the user's full name
// 5. getInitials           Get the users initials
// Entrant-specific methods:
// 6. joinEvent             Allows the entrant to join an event
// 7. leaveEvent            Allows the entrant to leave an event
// 8. becomeOrganizer       Pass in the facilityId to become an organizer
// Organizer-specific methods:
// 9. startEvent            Allows the organizer to start an event
// 10. endEvent             Allows the organizer to end an event
// 11. getters and setters
// Admin-specific methods:
// 12. deleteEvent          Allows the admin to delete an event
// 13. deleteUser           Allows the admin to delete a user
// 14. deleteFacility       Allows the admin to delete a facility
// 15. getters and setters for all users

// =============================================================================
// Any modification in User class should notify me first.
// If you have any questions / requirements, please contact me @V615 on discord.
// =============================================================================

// How to load user data:
// user.loadFromFirestore(new User.DataLoadedCallback() {
//    @Override
//    public void onDataLoaded(HashMap<String, Object> userData) {
//         // Your code here
//         callYourMethod();
//    }
//
//    // The following method is not necessary
//    @Override
//    public void onError(String error) {
//        // Handle the error message if the load fails
//        // Change MainActivity to the activity that you want to display the toast message
//        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
//    }
//});


/**
 * Represents a user which is the superclass to Entrant, Admin and Organizer.
 */
public class User {
    public String userId;
    public String role;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String profileImage;
    public HashMap<String, Boolean> notificationSettings;

    // Entrant-specific fields
    public ArrayList<String> joinedEventList;
    // Organizer-specific fields
    public String facilityId;
    public ArrayList<String> heldEventList;
    // Admin-specific fields

    /**
     * Constructs a User object.
     * @param userId      Unique identifier for the user.
     */
    public User(String userId) {
        this.userId = userId;

        role = "entrant";       // The default role is "entrant", load from firestore if needed
        firstName = null;
        lastName = null;
        email = null;
        phoneNumber = null;
        profileImage = null;
        notificationSettings = new HashMap<>();

        joinedEventList = new ArrayList<>();
        heldEventList = new ArrayList<>();
        facilityId = null;
    }

    /**
     * Constructs a User object from a dictionary.
     * @param userData      Dictionary containing user data.
     */
    public void fromDictionary(HashMap<String, Object> userData) {
        role = (String) userData.get("role");
        firstName = (String) userData.get("firstName");
        lastName = (String) userData.get("lastName");
        email = (String) userData.get("email");
        phoneNumber = (String) userData.get("phoneNumber");
        profileImage = (String) userData.get("profileImage");
        notificationSettings = (HashMap<String, Boolean>) userData.get("notificationSettings");

        switch (role) {
            case "entrant":
                // Entrant-specific fields
                joinedEventList = (ArrayList<String>) userData.get("eventList");
                break;
            case "organizer":
                // Organizer-specific fields
                facilityId = (String) userData.get("facilityId");
                heldEventList = (ArrayList<String>) userData.get("eventList");
                break;
            case "admin":
                // Admin-specific fields
                break;
        }
    }

    /**
     * Callback interface for loading user data.
     */
    public interface DataLoadedCallback {
        void onDataLoaded(HashMap<String, Object> userData);
        void onError(String error);
    }

    /**
     * Loads user data from Firestore.
     */
    public void loadFromFirestore(final DataLoadedCallback callback) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userRef = usersRef.document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the user data from the document
                        HashMap<String, Object> userData = (HashMap<String, Object>) documentSnapshot.getData();
                        if (userData != null) {
                            fromDictionary(userData);       // Load user data from the hash map
                            System.out.println(getRole() + " " + getFullName() + " successfully loaded.");
                            callback.onDataLoaded(userData);
                        }

                    } else {
                        System.out.println("User with ID " + userId + " does not exist.");
                        callback.onError("Welcome, new user!");
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error loading user data: " + e.getMessage());
                    callback.onError("Failed to load user data, please check your internet connection");
                });

    }

    /**
     * Constructs a User dictionary.
     * @return A hashmap representing the user instance.
     */
    public HashMap<String, Object> toDictionary() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("profileImage", profileImage);
        map.put("notificationSettings", notificationSettings);

        switch (role.toLowerCase(Locale.ROOT)) {
            case "entrant":
                // Entrant-specific fields
                map.put("eventList", joinedEventList);
                break;
            case "organizer":
                // Organizer-specific fields
                map.put("facilityId", facilityId);
                map.put("eventList", heldEventList);
                break;
            case "admin":
                // Admin-specific fields
                break;
        }
        return map;
    }

    /**
     * Uploads the user data to Firestore.
     */
    public void uploadToFirestore() {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userRef = usersRef.document(userId);

        // Update the user data to Firestore
        userRef.set(toDictionary())
                .addOnSuccessListener(aVoid -> {
                    System.out.println(role + " with ID " + userId + " successfully updated.");

                })
                .addOnFailureListener(e -> {
                    System.err.println("Error updating user: " + e.getMessage());
                });
    }

    /**
     * Handle notification settings. Only store the false value.
     * @param notification  notification setting that is being handled.
     * @param status        true if the notification is enabled, false otherwise.
     */
    public void handleNotification(String notification, Boolean status) {
        if (status) {
            notificationSettings.remove(notification);
        } else {
            notificationSettings.put(notification, false);
        }
    }

    /**
     * Get the user's full name.
     * @return User's full name in the format "FirstName LastName".
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Get the users initials.
     * @return Users initials in the format "FL".
     */
    public String getInitials() {
        if (firstName == null && lastName == null) {
            return "N/A";
        }
        return getFirstName().charAt(0) + "" + getLastName().charAt(0);
    }


    // Entrant-specific methods
    /**
     * Allows the entrant to join an event.
     * @param eventId ID of the event to join.
     */
    public void joinEvent(String eventId) {
        joinedEventList.add(eventId);
    }

    /**
     * Allows the entrant to leave an event.
     * @param eventId ID of the event to leave.
     */
    public void leaveEvent(String eventId) {
        joinedEventList.remove(eventId);
    }

    public ArrayList<String> getJoinedEventList() {return joinedEventList;}

    /**
     * Allows the entrant to become an organizer by joining a facility
     * @param facilityId ID of the facility to become an organizer.
     */
    public void becomeOrganizer(String facilityId) {
        this.role = "Organizer";
        this.facilityId = facilityId;

        uploadToFirestore();
    }


    // Organizer-specific methods
    /**
     * Allows the organizer to start an event.
     * @param eventId ID of the event to start.
     */
    public void startEvent(String eventId) {
        heldEventList.add(eventId);
    }

    /**
     * Allows the organizer to end an event.
     * @param eventId ID of the event to end.
     */
    public void endEvent(String eventId) {
        heldEventList.remove(eventId);
    }

    public ArrayList<String> getHeldEventList() {return heldEventList;}

    public String getFacilityId() {return facilityId;}
    public void setFacilityId(String facilityId) {this.facilityId = facilityId;}


    // Admin-specific methods
    /**
     * Allows the admin to delete an event.
     * @param eventId ID of the event to delete.
     */
    public void deleteEvent(String eventId) {
        // Get a reference to the event document
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");
        DocumentReference eventRef = eventsRef.document(eventId);

        // Delete the event document
        eventRef.delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Event with ID " + eventId + " successfully deleted.");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error deleting event: " + e.getMessage());
                });
    }

    /**
     * Allows the admin to delete a user.
     * @param userId ID of the user to remove.
     */
    public void deleteUser(String userId) {
        // Get a reference to the user document
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userRef = usersRef.document(userId);

        // Delete the user document
        userRef.delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("User with ID " + userId + " successfully deleted.");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error deleting user: " + e.getMessage());
                });
    }

    public void deleteFacility(String facilityId) {
        // Get a reference to the facility document
        CollectionReference facilitiesRef = FirebaseFirestore.getInstance().collection("facilities");
        DocumentReference facilityRef = facilitiesRef.document(facilityId);

        // Delete the facility document
        facilityRef.delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Facility with ID " + facilityId + " successfully deleted.");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error deleting facility: " + e.getMessage());
                });
    }


    // Getters and setters for all roles of users
    public HashMap<String, Boolean> getNotificationSettings() {return notificationSettings;}

    public String getUserId() { return userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFirstName() {
        if (firstName == null) {
            return " ";
        }
        return firstName;
    }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        if (lastName == null) {
            return " ";
        }
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName; }

    public String getEmail() {
        if (email == null) {
            return " ";
        }
        return email;
    }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() {
        if (phoneNumber == null) {
            return " ";
        }
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfileImage() {return profileImage;}
    public void setProfileImage(String profileImage) {this.profileImage = profileImage;}

}