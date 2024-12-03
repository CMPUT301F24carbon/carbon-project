package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an Entrant user, extending the {@link User} class.
 * The Entrant class provides functionality to manage the events that the entrant has joined,
 * saving the entrant's data to Firestore, and handling related attributes such as the user's role and event list.
 */
public class Entrant extends User implements Serializable {
    private List<String> joinedEvents;

    /**
     * Constructs an Entrant instance using a map of attributes.
     * The map should contain keys like "userId", "name", "email", "phoneNumber", and optionally "joinedEvents".
     * The role is automatically set to "entrant".
     *
     * @param map A map containing the entrant's attributes. The map may include a "joinedEvents" key with a list of event IDs.
     */
    public Entrant(Map<String, Object> map) {
        super(map);
        this.role = "entrant"; // Set the role to "entrant"
        if (map.containsKey("joinedEvents")) {
            this.joinedEvents = (List<String>) map.get("joinedEvents");
        } else {
            this.joinedEvents = new ArrayList<>();
        }
    }

    /**
     * Constructs an Entrant instance using individual attributes.
     * The role is automatically set to "entrant", and an empty list of joined events is initialized.
     *
     * @param userId       The unique ID of the user.
     * @param name         The name of the user.
     * @param email        The email address of the user.
     * @param phoneNumber  The phone number of the user.
     */

    public Entrant(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "entrant");
        this.joinedEvents = new ArrayList<>();
    }

    /**
     * Converts the Entrant object to a map of key-value pairs.
     * This method calls the superclass's {@link User#toMap()} method to convert common user attributes.
     * It also adds the "joinedEvents" list to the map.
     *
     * @return A map representation of the Entrant object, including the "joinedEvents" attribute.
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("joinedEvents", joinedEvents);
        return map;
    }

    /**
     * Saves the Entrant instance to Firebase Firestore under the "users" collection.
     * The Entrant's document will be stored using the user's ID as the document ID.
     * A success or failure message will be printed to the console.
     */
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

    /**
     * Retrieves the list of event IDs that the entrant has joined.
     *
     * @return A list of event IDs that the entrant has joined.
     */
    public List<String> getJoinedEvents() {
        return joinedEvents;
    }
}
