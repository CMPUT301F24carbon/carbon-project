package com.example.carbon_project.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Organizer extends User implements Serializable {
    private List<String> createdEvents;
    private List<String> facilityIds;
    private List<String> joinedEvents;

    public Organizer(Map<String, Object> map) {
        super(map);
        this.role = "organizer"; // Set the role to "organizer"
        if (map.containsKey("createdEvents")) {
            this.createdEvents = (List<String>) map.get("createdEvents");
        } else {
            this.createdEvents = new ArrayList<>();
        }
        if (map.containsKey("facilityIds")) {
            this.facilityIds = (List<String>) map.get("facilityIds");
        } else {
            this.facilityIds = new ArrayList<>();
        }
        if (map.containsKey("joinedEvents")) {
            this.joinedEvents = (List<String>) map.get("joinedEvents");
        } else {
            this.joinedEvents = new ArrayList<>();
        }
    }

    public Organizer(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "organizer");
        this.createdEvents = new ArrayList<>();
        this.facilityIds = new ArrayList<>();
    }

    // Specific methods for organizers
    public void addEvent(String eventId) { createdEvents.add(eventId); }
    public void removeEvent(String eventId) { createdEvents.remove(eventId); }
    public List<String> getCreatedEvents() { return createdEvents; }

    // Methods for managing facilities
    public void addFacility(String facilityId) { facilityIds.add(facilityId); }
    public void removeFacility(String facilityId) { facilityIds.remove(facilityId); }
    public List<String> getFacilityIds() { return facilityIds; }

    // Convert Organizer object to a Map for Firestore storage
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("createdEvents", createdEvents);
        map.put("joinedEvents", joinedEvents);
        return map;
    }

    // Save the Organizer to Firestore
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
