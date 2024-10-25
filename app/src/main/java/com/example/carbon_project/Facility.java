package com.example.carbon_project;

/**
 * Class represents a Facility; which belong to an organizer.
 */
public class Facility {
    private String facilityId;
    private String name;
    private String location;
    private int capacity;
    private String description;

    /**
     * Constructor to initialize the facility.
     * @param facilityId Unique identifier for the facility.
     * @param name Name of the facility.
     * @param location Location of the facility.
     * @param capacity Capacity of the facility.
     * @param description Description of the facility.
     */
    public Facility(String facilityId, String name, String location, int capacity, String description) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
    }

    /**
     * Returns the unique identifier for the facility.
     * @return The facilityID.
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     * Returns the name of the facility.
     * @return The name of the facility.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the facility.
     * @param name The name of the facility.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the facility.
     * @return The location of the facility.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the facility.
     * @param location The location of the facility.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the capacity of the facility.
     * @return The capacity of the facility.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the facility.
     * @param capacity The capacity of the facility.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns the description of the facility.
     * @return The description of the facility.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the facility.
     * @param description The description of the facility.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}