package com.example.carbon_project;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Admin extends User {
    public Admin(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "admin");
    }

    // Convert Admin object to a Map for Firestore storage
    @Override
    public Map<String, Object> toMap() {
        return super.toMap();  // Admin does not have additional fields, so use User's toMap method directly
    }

    // Save the Admin to Firestore
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")  // Store under the "users" collection
                .document(getUserId())  // Document ID is the user ID
                .set(toMap())  // Save the data as a map
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Admin saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving admin: " + e.getMessage());
                });
    }

    // Specific methods for admins
    public void removeEvent(String eventId) {
        // Implementation to remove event
    }
    public void removeProfile(String userId) {
        // Implementation to remove profile
    }
}
