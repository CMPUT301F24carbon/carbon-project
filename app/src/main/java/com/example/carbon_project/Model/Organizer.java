package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Organizer class represents an organizer in the system, extending the User class.
 * Organizers are responsible for creating and managing events, and associating themselves with facilities.
 * This class provides methods for adding/removing events and facilities, and saving the organizer data to Firestore.
 *
 * The Organizer class inherits the basic user properties (like name, email, phone) and adds specific functionality
 * related to organizing events and managing associated facilities. It uses Firebase Firestore to store and retrieve data.
 */
public class Organizer extends User implements Serializable {

    private List<String> createdEvents;  // List of event IDs created by the organizer
    private List<String> facilityIds;    // List of facility IDs associated with the organizer
    private List<String> joinedEvents;   // List of event IDs the organizer has joined

    /**
     * Constructor that creates an Organizer object from a map (usually fetched from Firestore).
     * This initializes the role to "organizer" and populates lists for created events, facility IDs,
     * and joined events from the provided map. If any list is missing from the map, an empty list is created.
     *
     * @param map A map containing the user's data (from Firestore), including created events, facility IDs, and joined events.
     */
    public Organizer(Map<String, Object> map) {
        super(map);  // Calls the constructor of the User class
        this.setRole("organizer");  // Set the role to "organizer"

        // Initialize the lists from the map or set them to empty lists if not present
        this.createdEvents = map.containsKey("createdEvents") ? (List<String>) map.get("createdEvents") : new ArrayList<>();
        this.facilityIds = map.containsKey("facilityIds") ? (List<String>) map.get("facilityIds") : new ArrayList<>();
        this.joinedEvents = map.containsKey("joinedEvents") ? (List<String>) map.get("joinedEvents") : new ArrayList<>();
    }

    /**
     * Constructor that creates an Organizer object with essential details (user ID, name, email, phone number).
     * The role is automatically set to "organizer".
     *
     * @param userId      The unique identifier for the user.
     * @param name        The name of the organizer.
     * @param email       The email address of the organizer.
     * @param phoneNumber The phone number of the organizer.
     */
    public Organizer(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "organizer");  // Calls the User constructor and sets the role
        this.createdEvents = new ArrayList<>();
        this.facilityIds = new ArrayList<>();
    }

    // Specific methods for organizers

    /**
     * Adds an event to the list of events created by the organizer.
     *
     * @param eventId The ID of the event to add.
     */
    public void addEvent(String eventId) {
        createdEvents.add(eventId);
    }

    /**
     * Removes an event from the list of events created by the organizer.
     *
     * @param eventId The ID of the event to remove.
     */
    public void removeEvent(String eventId) {
        createdEvents.remove(eventId);
    }

    /**
     * Returns the list of events created by the organizer.
     *
     * @return A list of event IDs that the organizer has created.
     */
    public List<String> getCreatedEvents() {
        return createdEvents;
    }

    // Methods for managing facilities

    /**
     * Adds a facility to the list of facilities associated with the organizer.
     *
     * @param facilityId The ID of the facility to add.
     */
    public void addFacility(String facilityId) {
        facilityIds.add(facilityId);
    }

    /**
     * Removes a facility from the list of facilities associated with the organizer.
     *
     * @param facilityId The ID of the facility to remove.
     */
    public void removeFacility(String facilityId) {
        facilityIds.remove(facilityId);
    }

    /**
     * Returns the list of facility IDs the organizer is associated with.
     *
     * @return A list of facility IDs.
     */
    public List<String> getFacilityIds() {
        return facilityIds;
    }

    /**
     * Converts the Organizer object to a Map representation, suitable for Firestore storage.
     * This includes the organizer's created events, joined events, and associated facilities.
     *
     * @return A Map containing the organizer's data for Firestore storage.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();  // Get the basic user data from the superclass
        map.put("createdEvents", createdEvents);  // Add created events to the map
        map.put("joinedEvents", joinedEvents);    // Add joined events to the map
        return map;
    }

    /**
     * Saves the Organizer object to Firestore under the "users" collection.
     * The data is stored using the organizer's unique user ID as the document identifier.
     */
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())
                .set(toMap())  // Convert the Organizer object to a map and save it to Firestore
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Organizer saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving organizer: " + e.getMessage());
                });
    }
}
