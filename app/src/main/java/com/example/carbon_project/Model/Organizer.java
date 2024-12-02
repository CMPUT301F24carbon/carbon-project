package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Organizer class represents a user who is an event organizer in the system.
 * It extends the User class and provides additional methods for managing created events
 * and associated facilities. Organizers can add or remove events, manage facilities, 
 * and store their data in Firestore.
 */
public class Organizer extends User implements Serializable {
    
    private List<String> createdEvents;  // List of event IDs created by the organizer
    private List<String> facilityIds;    // List of facility IDs associated with the organizer

    /**
     * Constructor for creating a new Organizer instance.
     *
     * @param userId      The unique user ID of the organizer.
     * @param name        The name of the organizer.
     * @param email       The email address of the organizer.
     * @param phoneNumber The phone number of the organizer.
     */
    public Organizer(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "organizer");
        this.createdEvents = new ArrayList<>();
        this.facilityIds = new ArrayList<>();
    }

    // Specific methods for managing events

    /**
     * Adds an event to the list of events created by the organizer.
     *
     * @param eventId The ID of the event to be added.
     */
    public void addEvent(String eventId) {
        createdEvents.add(eventId);
    }

    /**
     * Removes an event from the list of events created by the organizer.
     *
     * @param eventId The ID of the event to be removed.
     */
    public void removeEvent(String eventId) {
        createdEvents.remove(eventId);
    }

    /**
     * Retrieves the list of event IDs created by the organizer.
     *
     * @return A list of event IDs.
     */
    public List<String> getCreatedEvents() {
        return createdEvents;
    }

    // Methods for managing facilities

    /**
     * Adds a facility to the list of facilities associated with the organizer.
     *
     * @param facilityId The ID of the facility to be added.
     */
    public void addFacility(String facilityId) {
        facilityIds.add(facilityId);
    }

    /**
     * Removes a facility from the list of facilities associated with the organizer.
     *
     * @param facilityId The ID of the facility to be removed.
     */
    public void removeFacility(String facilityId) {
        facilityIds.remove(facilityId);
    }

    /**
     * Retrieves the list of facility IDs associated with the organizer.
     *
     * @return A list of facility IDs.
     */
    public List<String> getFacilityIds() {
        return facilityIds;
    }

    /**
     * Converts the Organizer object to a Map for Firestore storage.
     * This includes the base properties of the User class as well as the
     * specific properties of the Organizer (i.e., created events).
     *
     * @return A map containing the Organizer's data for Firestore storage.
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("createdEvents", createdEvents);
        map.put("facilityIds", facilityIds);
        return map;
    }

    /**
     * Saves the Organizer's data to Firestore under the "users" collection.
     * This method stores the Organizer object by converting it to a Map and saving it
     * using the user ID as the document identifier.
     */
    public void saveToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getUserId())
                .set(toMap())
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Organizer saved successfully");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error saving organizer: " + e.getMessage());
                });
    }
}
