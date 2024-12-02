package com.example.carbon_project.Model;

import android.util.Log;

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
    public String role;

    //This is a token for notifications
    private String fcm;

    public User(Map<String, Object> map) {
        this.userId = (String) map.get("userId");
        this.name = (String) map.get("name");
        this.email = (String) map.get("email");
        this.phoneNumber = (String) map.get("phoneNumber");
        this.profilePictureUrl = (String) map.get("profilePictureUrl");
        this.fcm = (String) map.get("fcm");
        this.role = (String) map.get("role");
    }

    public User(String userId, String name, String email, String phoneNumber, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profilePictureUrl = "";

        this.requestToken();
    }

    public User(String userId, String name, String email, String phoneNumber, String role, String profilePictureUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profilePictureUrl = profilePictureUrl;

        this.requestToken();
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
        map.put("name", name != null ? name : "");
        map.put("email", email != null ? name : "");
        map.put("phoneNumber", phoneNumber != null ? name : "");
        map.put("role", role);
        map.put("profilePictureUrl", profilePictureUrl != null ? profilePictureUrl : "");
        map.put("fcm", fcm != null ? fcm : "");

        return map;
    }

    private void requestToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the FCM token
                        this.fcm = task.getResult();
                        this.saveToFirestore();
                        Log.d("Success", this.fcm);
                    } else {
                        System.err.println("Failed to retrieve FCM token");
                    }
                });
    }
}