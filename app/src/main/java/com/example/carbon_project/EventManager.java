package com.example.carbon_project;

import java.util.ArrayList;

/**
 * EventManager is a singleton class that manages a list of Event objects.
 * It provides methods to add events and retrieve the list of events.
 */
public class EventManager {

    private static EventManager instance;
    private ArrayList<Event> events;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the events list.
     */
    private EventManager() {
        events = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of EventManager.
     * Creates a new instance if it doesn't exist.
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
     * @param event The event to be added.
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Returns the list of events.
     * @return The list of events.
     */
    public ArrayList<Event> getEvents() {
        return events;
    }
}
