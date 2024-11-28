package com.example.carbon_project;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Entrant extends User implements Serializable {
    private List<String> joinedEvents; // Event IDs of joined waiting lists

    public Entrant(String userId, String name, String email, String phoneNumber) {
        super(userId, name, email, phoneNumber, "entrant");
        this.joinedEvents = new ArrayList<>();
    }

    // Convert Entrant object to a Map for Firestore storage
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("joinedEvents", joinedEvents);
        return map;
    }

    // Save the Entrant to Firestore
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

    // Specific methods for entrants
    public void joinEvent(String eventId) { joinedEvents.add(eventId); }
    public void leaveEvent(String eventId) { joinedEvents.remove(eventId); }
    public List<String> getJoinedEvents() { return joinedEvents; }
}
