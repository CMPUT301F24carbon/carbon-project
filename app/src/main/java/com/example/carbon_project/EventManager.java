package com.example.carbon_project;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventManager {

    private static EventManager instance;
    private ArrayList<Event> events;
    private FirebaseFirestore db;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the events list and Firestore instance.
     */
    private EventManager() {
        events = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Returns the singleton instance of EventManager.
     * Creates a new instance if it doesn't exist.
     *
     * @return The singleton instance of EventManager.
     */
    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    /**
     * Adds a new event to the events list.
     *
     * @param event The event to be added.
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Returns the list of events.
     *
     * @return The list of events.
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Fetches events from Firestore and updates the local list.
     *
     * @param onComplete A callback to handle completion, providing success status and the list of events.
     */
    public void fetchEvents(OnCompleteListener<ArrayList<Event>> onComplete) {
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                events.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    events.add(event);
                }
                Log.d("EventManager", "Fetched events: " + events.size());
                onComplete.onComplete(true, events);
            } else {
                Log.e("EventManager", "Error fetching events: " + task.getException());
                onComplete.onComplete(false, null);
            }
        });
    }

    /**
     * Interface for completion listener when fetching events.
     */
    public interface OnCompleteListener<T> {
        void onComplete(boolean success, T result);
    }
}
