package com.example.carbon_project;

import com.google.firebase.firestore.FirebaseFirestore;

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
     * Interface for completion listener when fetching events.
     */
    public interface OnCompleteListener<T> {
        void onComplete(boolean success, T result);
    }
}
