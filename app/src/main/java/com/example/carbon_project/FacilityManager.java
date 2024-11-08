package com.example.carbon_project;

import java.util.ArrayList;

public class FacilityManager {

    private static FacilityManager instance;
    private ArrayList<Facility> facilities;

    private FacilityManager() {
        facilities = new ArrayList<>();
    }

    public static FacilityManager getInstance() {
        if (instance == null) {
            instance = new FacilityManager();
        }
        return instance;
    }

    public void addFacility(Facility facility) {
        facilities.add(facility);
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }
}