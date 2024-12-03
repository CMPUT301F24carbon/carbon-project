package com.example.carbon_project.Model;

import android.widget.Toast;

import com.example.carbon_project.Controller.OrganizerFacilitiesActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a facility in the system.
 */
public class Facility implements Serializable {
    private String facilityId;
    private String name;
    private String location;
    private int capacity;
    private String organizerId;

    /**
     * Default constructor for Facility.
     */
    public Facility() {
    }

    /**
     * Constructor for Facility with given parameters.
     * @param facilityId
     * @param name
     * @param location
     * @param capacity
     * @param organizerId
     */
    public Facility(String facilityId, String name, String location, int capacity, String organizerId) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.organizerId = organizerId;
    }

    /**
     * Getter for facilityId.
     * @return
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     * Setter for facilityId.
     * @param facilityId
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * Getter for name.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for location.
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location.
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for capacity.
     * @return
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Setter for capacity.
     * @param capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Getter for organizerId.
     * @return
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Setter for organizerId.
     * @param organizerId
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Deletes the facility from the database.
     */
    public void deleteFacility() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereEqualTo("facility.facilityId", facilityId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("events").document(document.getId()).delete();
                    }

                    db.collection("facilities")
                            .document(facilityId)
                            .delete();
                });
    }

    /**
     * Convert Facility object to a Map for Firestore storage.
     * @return
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("facilityId", facilityId);
        map.put("name", name);
        map.put("location", location);
        map.put("capacity", capacity);
        map.put("organizerId", organizerId);
        return map;
    }
}