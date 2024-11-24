package com.example.carbon_project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

// The methods you should know how to use are:
// 0. User(userId)          You only need to pass in the userId i.e. device ID
// 1. loadFromFirestore     Load user data from Firestore and update the hash map
// 2. uploadToFirestore     Upload the hash map to Firestore
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

// How to create a user:
// User user = new User(userId);

// How to load user data:
//user.loadFromFirestore(new User.DataLoadedCallback() {
//    @Override
//    public void onDataLoaded(HashMap<String, Object> userData) {  // You can also use the userData HashMap to get the user details
//        // Your code here
//         callYourMethod();
//         user.uploadToFirestore();
//    }
//
//    // The following method is not mandatory
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
    // The user data is stored in the userData HashMap, which includes the following keys:
    // "role", "firstName", "lastName", "email", "phoneNumber", "profileImageUri", "notificationSettings"
    // For entrants, the following keys are also available:
    // "joinedEventList"
    // For organizers, the following keys are also available:
    // "facilityId", "heldEventList"
    // For admins, the following keys are also available:
    // N/A
    public HashMap<String, Object> userData;
    public String userId;

    /**
     * Constructs a User object.
     * @param userId      Unique identifier for the user.
     */
    public User(String userId) {
        this.userId = userId;

        userData = new HashMap<>();
        userData.put("role", "Entrant");        // The default role is "entrant", load from firestore if needed
        userData.put("firstName", null);
        userData.put("lastName", null);
        userData.put("email", null);
        userData.put("phoneNumber", null);
        userData.put("profileImageUri", null);
        userData.put("notificationSettings", new HashMap<>());
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
     * @param callback Callback to handle the loaded data.
     */
    public void loadFromFirestore(final DataLoadedCallback callback) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userRef = usersRef.document(userId);

        // Validate role-specific fields
        String role = getRole();
        switch (role) {
            case "Entrant":
                if (!userData.containsKey("joinedEventList")) {
                    userData.put("joinedEventList", new ArrayList<>());
                }
                break;
            case "Organizer":
                if (!userData.containsKey("facilityId")) {
                    userData.put("facilityId", null);
                }
                if (!userData.containsKey("heldEventList")) {
                    userData.put("heldEventList", new ArrayList<>());
                }
                break;
            case "Admin":
                // Admin does not need additional fields, but you can add validations here if necessary.
                break;
            default:
                System.err.println("Invalid role: " + role);
                return;
        }


        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the user data from the document
                        HashMap<String, Object> firestoreData = (HashMap<String, Object>) documentSnapshot.getData();
                        if (firestoreData != null) {
                            userData.putAll(firestoreData);
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
     * Uploads the user data to Firestore.
     */
    public void uploadToFirestore() {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userRef = usersRef.document(userId);

        // Update the user data to Firestore
        userRef.set(userData)
                .addOnSuccessListener(aVoid -> {
                    System.out.println(getRole() + " with ID " + userId + " successfully updated.");

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
        HashMap notificationSettings = (HashMap<String, Boolean>) userData.get("notificationSettings");

        if (status) {
            if (notificationSettings != null) {
                notificationSettings.remove(notification);
            }
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
        if (getFirstName() == null && getLastName() == null) {
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
        ArrayList<String> joinedEventList = getJoinedEventList();
        // Check if the joinedEventList is null
        if (joinedEventList == null) {
            joinedEventList = new ArrayList<>();
        }
        joinedEventList.add(eventId);
        userData.put("joinedEventList", joinedEventList);
    }

    /**
     * Allows the entrant to leave an event.
     * @param eventId ID of the event to leave.
     */
    public void leaveEvent(String eventId) {
        ArrayList<String> joinedEventList = getJoinedEventList();
        joinedEventList.remove(eventId);
        userData.put("joinedEventList", joinedEventList);
    }

    /**
     * Allows the entrant to become an organizer by joining a facility
     * @param facilityId ID of the facility to become an organizer.
     */
    public void becomeOrganizer(String facilityId) {
        setRole("organizer");
        setFacilityId(facilityId);

        uploadToFirestore();
    }

    public ArrayList<String> getJoinedEventList() {return (ArrayList<String>) userData.get("joinedEventList");}


    // Organizer-specific methods
    /**
     * Allows the organizer to start an event.
     * @param eventId ID of the event to start.
     */
    public void startEvent(String eventId) {
        ArrayList<String> heldEventList = getHeldEventList();
        // Check if the heldEventList is null
        if (heldEventList == null) {
            heldEventList = new ArrayList<>();
        }
        heldEventList.add(eventId);
        userData.put("heldEventList", heldEventList);
    }

    /**
     * Allows the organizer to end an event.
     * @param eventId ID of the event to end.
     */
    public void endEvent(String eventId) {
        ArrayList<String> heldEventList = getHeldEventList();
        heldEventList.remove(eventId);
        userData.put("heldEventList", heldEventList);
    }

    public ArrayList<String> getHeldEventList() {return (ArrayList<String>) userData.get("heldEventList");}

    public String getFacilityId() {return (String) userData.get("facilityId");}
    public void setFacilityId(String facilityId) { userData.put("facilityId", facilityId); }


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

    /**
     * Allows the admin to delete a facility.
     * @param facilityId ID of the facility to remove.
     */
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
    public HashMap<String, Boolean> getNotificationSettings() {return (HashMap<String, Boolean>) userData.get("notificationSettings");}

    public String getUserId() {return userId;}

    public String getRole() {return (String) userData.get("role");}
    public void setRole(String role) { userData.put("role", role); }

    public String getFirstName() {
        String firstName = (String) userData.get("firstName");
        if (firstName == null) {
            return " ";
        }
        return firstName;
    }
    public void setFirstName(String firstName) { userData.put("firstName", firstName); }

    public String getLastName() {
        String lastName = (String) userData.get("lastName");
        if (lastName == null) {
            return " ";
        }
        return lastName;
    }
    public void setLastName(String lastName) { userData.put("lastName", lastName); }

    public String getEmail() {
        String email = (String) userData.get("email");
        if (email == null) {
            return " ";
        }
        return email;
    }
    public void setEmail(String email) { userData.put("email", email); }

    public String getPhoneNumber() {
        String phoneNumber = (String) userData.get("phoneNumber");
        if (phoneNumber == null) {
            return " ";
        }
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) { userData.put("phoneNumber", phoneNumber); }

    public String getProfileImageUri() {return (String) userData.get("profileImageUri");}
    public void setProfileImageUri(String profileImageUri) { userData.put("profileImageUri", profileImageUri); }


    /**
     * Factory method to create a User object based on the role.
     * @param userId Unique identifier for the user.
     * @param role   Role of the user ("Entrant", "Organizer", "Admin").
     * @return Initialized User object.
     */
    public static User createUser(String userId, String role) {
        User user = new User(userId);
        user.setRole(role);

        switch (role) {
            case "Entrant":
                user.userData.put("joinedEventList", new ArrayList<>());
                break;
            case "Organizer":
                user.userData.put("facilityId", null);
                user.userData.put("heldEventList", new ArrayList<>());
                break;
            case "Admin":
                // Admin does not need additional fields.
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        return user;
    }


}