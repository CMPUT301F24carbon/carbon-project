package com.example.carbon_project.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a facility that can host an event. A facility has properties such as an ID, name,
 * location, capacity, and the organizer associated with it. This class is used to store and manage
 * information related to the facilities available for events.
 */
public class Facility implements Serializable {

    private String facilityId;
    private String name;
    private String location;
    private int capacity;
    private String organizerId;

    /**
     * Default constructor for Facility.
     * Initializes an empty Facility object.
     */
    public Facility() {
    }

    /**
     * Constructs a Facility with the specified details.
     *
     * @param facilityId    The unique identifier for the facility.
     * @param name          The name of the facility.
     * @param location      The location of the facility.
     * @param capacity      The maximum capacity of the facility.
     * @param organizerId   The ID of the organizer associated with the facility.
     */
    public Facility(String facilityId, String name, String location, int capacity, String organizerId) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.organizerId = organizerId;
    }

    // Getters and Setters

    /**
     * Returns the unique ID of the facility.
     * 
     * @return The facility's unique ID.
     */
    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * Returns the name of the facility.
     * 
     * @return The facility's name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the facility.
     * 
     * @return The facility's location.
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the capacity of the facility.
     * 
     * @return The maximum number of people the facility can accommodate.
     */
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns the ID of the organizer associated with the facility.
     * 
     * @return The ID of the organizer.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Converts the Facility object into a Map suitable for Firestore storage.
     * 
     * @return A Map containing the facility's details.
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
