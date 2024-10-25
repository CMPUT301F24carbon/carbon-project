package com.example.carbon_project;

/**
 * Class represents an event object.
 * Stores information about event details.
 */
public class Event {
    private String eventId;
    private String name;
    private String date;
    private String location;
    private int capacity;

    /**
     * Constructor for creating an Event.
     * @param eventId Unique identifier for the event.
     * @param name Name of the event.
     * @param date Date of the event.
     * @param location Location of the event.
     * @param capacity Maximum capacity of attendees for the event.
     */
    public Event(String eventId, String name, String date, String location, int capacity) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
    }

    public String getEventId() { return eventId; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}