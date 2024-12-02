package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an entrant user in the system.
 * An entrant is a user who has joined one or more events.
 * This class extends the `User` class and adds functionality specific to entrants, such as keeping track of the events they have joined.
 */
public class Entrant extends User implements Serializable {

    // A list to store the event IDs of events that the entrant has joined.
    private List<String> joinedEvents;

    /**
     * Constructor for creating an Entrant object.
     * 
     * @param userId      The unique identifier for the entrant.
     * @param name        The name of the entrant.
     * @param email       The email address of the entrant.
     * @param phoneNumber The phone number of the entrant.
     */
    public Entrant(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "entrant");
        this.joinedEvents = new ArrayList<>();
    }

    /**
     * Converts the Entrant object into a Map representation for Firestore storage.
     * This method overrides the `toMap` method from the `User` class and includes the list of events the entrant has joined.
     * 
     * @return A map containing the entrant's details along with their joined events.
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("joinedEvents", joinedEvents);  // Include the list of joined events in the map.
        return map;
    }

    /**
     * Saves the Entrant object to Firestore.
     * The entrant data is saved in the "users" collection with the entrant's `userId` as the document ID.
     * 
     * This method triggers Firestore's `set()` operation and logs a success or failure message.
     */
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())  // The document ID is the entrant's unique userId.
                .set(toMap())           // The data is set using the `toMap` method to serialize the Entrant object.
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Entrant saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving entrant: " + e.getMessage());
                });
    }
}
