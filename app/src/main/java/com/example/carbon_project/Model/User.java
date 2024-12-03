package com.example.carbon_project.Model;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The User class represents a user in the system. It stores basic user information such as
 * user ID, name, email, phone number, profile picture URL, and role. Additionally, it stores
 * an FCM (Firebase Cloud Messaging) token that is used for sending notifications to the user.
 * The class provides methods for saving the user’s data to Firestore and checking the user’s
 * role (e.g., "entrant", "organizer", or "admin").
 *
 * Users can have different roles in the system, and the role determines the user's permissions.
 * The system uses Firebase Cloud Messaging (FCM) to send notifications to users based on their role.
 */
public class User implements Serializable {

    private String userId;             // Unique identifier for the user
    private String name;               // User's name
    private String email;              // User's email address
    private String phoneNumber;        // User's phone number
    private String profilePictureUrl;  // URL to the user's profile picture
    private String role;               // Role of the user (e.g., "entrant", "organizer", "admin")

    private String fcm;                // FCM token used for sending notifications

    /**
     * Constructor for creating a User object with essential details and generating an FCM token.
     * The FCM token is retrieved from Firebase Cloud Messaging and saved to Firestore.
     *
     * @param userId      Unique identifier for the user.
     * @param name        The name of the user.
     * @param email       The email address of the user.
     * @param phoneNumber The phone number of the user.
     * @param role        The role of the user (e.g., "entrant", "organizer", "admin").
     */
    public User(String userId, String name, String email, String phoneNumber, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profilePictureUrl = null;

        // Retrieve FCM token from Firebase Cloud Messaging (FCM) and store it
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        this.fcm = task.getResult();
                        this.saveToFirestore();  // Save user to Firestore once FCM token is retrieved
                        Log.d("Success", this.fcm);
                    } else {
                        System.err.println("Failed to retrieve FCM token");
                    }
                });
    }

    /**
     * Constructor for creating a User object with essential details, including a profile picture URL.
     *
     * @param userId          Unique identifier for the user.
     * @param name            The name of the user.
     * @param email           The email address of the user.
     * @param phoneNumber     The phone number of the user.
     * @param role            The role of the user (e.g., "entrant", "organizer", "admin").
     * @param profilePictureUrl The URL of the user's profile picture.
     */
    public User(String userId, String name, String email, String phoneNumber, String role, String profilePictureUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profilePictureUrl = profilePictureUrl;
    }

    public User(Map<String, Object> map) {
    }

    // Getter and Setter methods for each field

    /**
     * Gets the unique user ID.
     *
     * @return The unique user ID.
     */
    public String getUserId() { return userId; }

    /**
     * Sets the unique user ID.
     *
     * @param userId The unique user ID to set.
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() { return name; }

    /**
     * Sets the name of the user.
     *
     * @param name The name of the user to set.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() { return email; }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber The phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Gets the URL to the user's profile picture.
     *
     * @return The URL to the user's profile picture.
     */
    public String getProfilePictureUrl() { return profilePictureUrl; }

    /**
     * Sets the URL to the user's profile picture.
     *
     * @param profilePictureUrl The profile picture URL to set.
     */
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    /**
     * Gets the role of the user (e.g., "entrant", "organizer", "admin").
     *
     * @return The role of the user.
     */
    public String getRole() { return role; }

    /**
     * Sets the role of the user (e.g., "entrant", "organizer", "admin").
     *
     * @param role The role to set.
     */
    public void setRole(String role) { this.role = role; }

    // Role Check Methods

    /**
     * Checks if the user has the "entrant" role.
     *
     * @return true if the user is an entrant, false otherwise.
     */
    public boolean isEntrant() { return role.equals("entrant"); }

    /**
     * Checks if the user has the "organizer" role.
     *
     * @return true if the user is an organizer, false otherwise.
     */
    public boolean isOrganizer() { return role.equals("organizer"); }

    /**
     * Checks if the user has the "admin" role.
     *
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isAdmin() { return role.equals("admin"); }

    /**
     * Saves the User object to Firestore under the "users" collection.
     * The user data is stored using the userId as the document identifier.
     */
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())
                .set(toMap())
                .addOnSuccessListener(aVoid -> {
                    System.out.println("User saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving user: " + e.getMessage());
                });
    }

    /**
     * Converts the User object into a Map format for Firestore storage.
     * This includes the user's basic profile details and FCM token.
     *
     * @return A map representation of the User object, ready for Firestore storage.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("role", role);
        map.put("profilePictureUrl", profilePictureUrl);
        map.put("fcm", fcm);
        return map;
    }
}
