package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;
/**
 * Represents an Admin user, extending the {@link User} class.
 * The Admin class provides functionality for managing admin-specific tasks such as removing events,
 * saving the admin user to Firestore, and extending user functionality with an admin role.
 */
public class Admin extends User implements Serializable {
    /**
     * Constructs an Admin instance using a map of attributes.
     * The map should contain keys like "userId", "name", "email", "phoneNumber", and any other relevant information.
     * The role is automatically set to "admin".
     *
     * @param map A map containing the admin's attributes.
     */
    public Admin(Map<String, Object> map) {
        super(map);
        this.role = "admin"; // Set the role to "admin"
    }

    /**
     * Constructs an Admin instance using individual attributes.
     * The role is automatically set to "admin".
     *
     * @param userId       The unique ID of the user.
     * @param name         The name of the user.
     * @param email        The email address of the user.
     * @param phoneNumber  The phone number of the user.
     */
    public Admin(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "admin");
    }

    /**
     * Converts the Admin object to a map of key-value pairs.
     * This method calls the superclass's {@link User#toMap()} method to convert the common user attributes.
     *
     * @return A map representation of the Admin object.
     */
    @Override
    public Map<String, Object> toMap() {
        return super.toMap();
    }

    /**
     * Saves the Admin instance to Firebase Firestore under the "users" collection.
     * The Admin's document will be stored using the user's ID as the document ID.
     * A success or failure message will be printed to the console.
     */
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

}
