package com.example.carbon_project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// The methods you should know how to use are:
// 0. Facility(facilityId)  Create an empty facility to load from Firestore
// 0. Facility(...)         Create a new facility with all details
// 1. loadFromFirestore     Load facility data from Firestore and update the hash map
// 2. uploadToFirestore     Upload the hash map to Firestore
// 4. addEvent              Adds an event to the facility
// 5. removeEvent           Removes an event from the facility
// 6. getters and setters

// =============================================================================
// Any modification in Facility class should notify me first.
// If you have any questions / requirements, please contact me @V615 on discord.
// =============================================================================

// How to create a new Facility:
// Facility facility = new Facility(String name, String location, int capacity, String description);

// How to create a facility to load from Firestore:
// Facility facility = new Facility(facilityId);
// and then load facility data

// How to load facility data:
//facility.loadFromFirestore(new Facility.DataLoadedCallback() {
//    @Override
//    public void onDataLoaded(HashMap<String, Object> facilityData) {  // You can also use the facilityData HashMap to get the event details
//        // Your code here
//        callYourMethod();
//        facility.uploadToFirestore();
//    }
//
//    // The following method is not mandatory
//    @Override
//    public void onError(String error) {
//        // Handle the error message if the load fails
//        // Change MainActivity to the activity that you want to display the toast message
//        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
//    }
//});

/**
 * Class represents a Facility; which belong to an organizer.
 */
public class Facility  {
    HashMap<String, Object> facilityData;
    private String facilityId;

    /**
     * Constructor to initialize the facility.
     * @param facilityId Unique identifier for the facility.
     */
    public Facility(String facilityId) {
        this.facilityId = facilityId;
        facilityData = new HashMap<>();
    }

    /**
     * Constructor to initialize the facility.
     * @param name Name of the facility.
     * @param location Location of the facility.
     * @param capacity Capacity of the facility.
     * @param description Description of the facility.
     */
    public Facility(String name, String location, int capacity, String description) {
        facilityId = UUID.randomUUID().toString();

        facilityData = new HashMap<>();
        facilityData.put("name", name);
        facilityData.put("location", location);
        facilityData.put("capacity", capacity);
        facilityData.put("description", description);
        facilityData.put("facilityEvents", new ArrayList<>());
    }

    /**
     * Callback interface for loading facility data.
     */
    public interface DataLoadedCallback {
        void onDataLoaded(HashMap<String, Object> facilityData);
        void onError(String error);
    }

    /**
     * Loads facility data from Firestore.
     * @param callback Callback to handle the loaded data.
     */
    public void loadFromFirestore(final DataLoadedCallback callback) {
        CollectionReference facilitiesRef = FirebaseFirestore.getInstance().collection("facilities");
        DocumentReference facilityRef = facilitiesRef.document(facilityId);

        facilityRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the user data from the document
                        HashMap<String, Object> firestoreData = (HashMap<String, Object>) documentSnapshot.getData();
                        if (firestoreData != null) {
                            facilityData.putAll(firestoreData);
                            System.out.println("Facility " + getName() + " successfully loaded.");
                            callback.onDataLoaded(facilityData);
                        }
                    } else {
                        System.out.println("Facility with ID " + facilityId + " does not exist.");
                        callback.onError("Event does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error loading facility data: " + e.getMessage());
                    callback.onError("Failed to load facility data, please check your internet connection");
                });
    }

    public void uploadToFirestore() {
        CollectionReference facilitiesRef = FirebaseFirestore.getInstance().collection("facilities");
        DocumentReference facilityRef = facilitiesRef.document(facilityId);

        // Update the facility data to Firestore
        facilityRef.set(facilityData)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Facility with ID " + facilityId + " successfully updated.");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error updating facility: " + e.getMessage());
                });
    }

    /**
     * Adds an event to the Facility's list
     * @param eventId The ID of the event to be added
     */
    public void addEvent(String eventId) {
        ArrayList<String> facilityEvents = (ArrayList<String>) facilityData.get("facilityEvents");
        facilityEvents.add(eventId);
    }

    /**
     * Removes an event from the Facility's list
     * @param eventId The ID of the event to be removed
     */
    public void removeEvent(String eventId) {
        ArrayList<String> facilityEvents = (ArrayList<String>) facilityData.get("facilityEvents");
        facilityEvents.remove(eventId);
    }


    // Getters and Setters
    public String getFacilityId() {return facilityId;}

    public ArrayList<String> getFacilityEvents() {return (ArrayList<String>) facilityData.get("facilityEvents");}

    public String getName() {return (String) facilityData.get("name");}
    public void setName(String name) {facilityData.put("name", name);}

    public String getLocation() {return (String) facilityData.get("location");}
    public void setLocation(String location) {facilityData.put("location", location);}

    public int getCapacity() {return (int) facilityData.get("capacity");}
    public void setCapacity(int capacity) {facilityData.put("capacity", capacity);}

    public String getDescription() {return (String) facilityData.get("description");}
    public void setDescription(String description) {facilityData.put("description", description);}

}