package com.example.carbon_project;

import java.util.ArrayList;

/**
 * FacilityManager is a singleton class that manages a list of Facility objects.
 * It provides methods to add facilities and retrieve the list of facilities.
 */
public class FacilityManager {

    private static FacilityManager instance;
    private ArrayList<Facility> facilities;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the facilities list.
     */
    private FacilityManager() {
        facilities = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of FacilityManager.
     * Creates a new instance if it doesn't exist.
     * @return The singleton instance of FacilityManager.
     */
    public static FacilityManager getInstance() {
        if (instance == null) {
            instance = new FacilityManager();
        }
        return instance;
    }

    /**
     * Adds a new facility to the facilities list.
     * @param facility The facility to be added.
     */
    public void addFacility(Facility facility) {
        facilities.add(facility);
    }

    /**
     * Returns the list of facilities.
     * @return The list of facilities.
     */
    public ArrayList<Facility> getFacilities() {
        return facilities;
    }
}