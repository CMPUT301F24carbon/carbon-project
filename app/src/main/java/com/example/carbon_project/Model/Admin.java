package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;

public class Admin extends User implements Serializable {
    public Admin(String userId, String name, String email, String phoneNumber, boolean allowNotifications) {
        super(userId, name, email, phoneNumber, "admin", allowNotifications);
    }

    @Override
    public Map<String, Object> toMap() {
        return super.toMap();
    }

    // Save the Admin to Firestore
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())
                .set(toMap())
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
