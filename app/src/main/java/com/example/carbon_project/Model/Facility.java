package com.example.carbon_project.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Facility implements Serializable {
    private String facilityId;
    private String name;
    private String location;
    private int capacity;
    private String organizerId;

    public Facility() {
    }

    public Facility(String facilityId, String name, String location, int capacity, String organizerId) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.organizerId = organizerId;
    }

    // Getters and Setters
    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    // Convert Facility object to a Map for Firestore storage
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