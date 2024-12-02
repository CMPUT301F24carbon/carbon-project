package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents an admin user in the system.
 * The admin extends the general User class and is used for administrative tasks and privileges.
 */
public class Admin extends User implements Serializable {

    /**
     * Constructor for creating an Admin object.
     * 
     * @param userId      The unique identifier for the admin.
     * @param name        The name of the admin.
     * @param email       The email address of the admin.
     * @param phoneNumber The phone number of the admin.
     */
    public Admin(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "admin");
    }

    /**
     * Converts the Admin object into a Map representation for Firestore storage.
     * This method overrides the `toMap` method of the User class.
     * 
     * @return A map containing the admin's details (inherited from User).
     */
    @Override
    public Map<String, Object> toMap() {
        return super.toMap();
    }

    /**
     * Saves the Admin object to Firestore.
     * The admin data is saved in the "users" collection, with the admin's userId as the document ID.
     * 
     * This method triggers Firestore's set operation and logs a success or failure message.
     */
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())  // The document ID is the admin's unique userId.
                .set(toMap())           // The data is set using the `toMap` method to serialize the Admin object.
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Admin saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving admin: " + e.getMessage());
                });
    }
}
