package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl;
    private String role;

    //This is a token for notifications
    private String fcm;
    private boolean allowNotifications;

    public User(String userId, String name, String email, String phoneNumber, String role, boolean allowNotifications) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profilePictureUrl = null;
        this.allowNotifications = allowNotifications;

        //generates a token that tells firebase who to send notifications to
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the FCM token
                        this.fcm = task.getResult();
                    } else {
                        System.err.println("Failed to retrieve FCM token");
                    }
                });
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean getNotifications() {return allowNotifications;}
    public void setNotifications(boolean notifications) { this.allowNotifications = notifications;}

    // Role Check
    public boolean isEntrant() { return role.equals("entrant"); }
    public boolean isOrganizer() { return role.equals("organizer"); }
    public boolean isAdmin() { return role.equals("admin"); }

    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())
                .set(toMap())
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Entrant saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving entrant: " + e.getMessage());
                });
    }

    // Convert User object to a Map for Firestore storage
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("role", role);
        map.put("profilePictureUrl", profilePictureUrl);
        map.put("notifications", allowNotifications);
        if(allowNotifications){
            map.put("fcm", fcm);
        }

        return map;
    }
}