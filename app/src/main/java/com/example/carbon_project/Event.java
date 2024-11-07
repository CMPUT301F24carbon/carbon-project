package com.example.carbon_project;

import java.util.ArrayList;
import java.util.Random;

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
    private int waitingListLimit;
    private ArrayList<Entrant> waitingList;
    private ArrayList<Entrant> selectedList;
    private boolean geolocationRequired;

    /**
     * Constructor for creating an Event without the optional limit on waitingList size.
     * @param eventId Unique identifier for the event.
     * @param name Name of the event.
     * @param date Date of the event.
     * @param location Location of the event.
     * @param capacity Maximum capacity of attendees for the event.
     * @param geolocationRequired True/False depending if the organizer wants to use geolocation for this event
     */
    public Event(String eventId, String name, String date, String location, int capacity, boolean geolocationRequired) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.geolocationRequired = geolocationRequired;
    }

    /**
     * Constructor for creating an Event with the optional limit on waitingList size.
     * @param eventId Unique identifier for the event.
     * @param name Name of the event.
     * @param date Date of the event.
     * @param location Location of the event.
     * @param capacity Maximum capacity of attendees for the event.
     * @param geolocationRequired True/False depending if the organizer wants to use geolocation for this event
     * @param waitingListLimit Max size for the waitingList
     */
    public Event(String eventId, String name, String date, String location, int capacity, boolean geolocationRequired, int waitingListLimit) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.waitingListLimit = waitingListLimit;
    }

    public String getEventId() { return eventId; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    /**
     * Randomly selects users from the waitingList and moves them into the selectedList of Entrants
     */
    public void lottery() {
        Random random = new Random();
        while(selectedList.size() < capacity) {
            int randomIndex = random.nextInt(waitingList.size());
            selectedList.add(waitingList.get(randomIndex));
            waitingList.remove(randomIndex);
        }
        // notification code for notifying winners goes here

    }

    /**
     * Adds a new entrant to the waiting list
     * @param entrant entrant to be added to the waiting list
     */
    public void addEntrant(Entrant entrant) {
        /*waitingListLimit is initialized to 0, so we check to see that its value is nonzero
          if its value is nonzero, check if we are below its limit before we add the entrant
          if its value is nonzero and we are not below the waitingListLimit, notify user that the waitinglist is full
          if neither of the above is true then we add the user without any checks
         */
        if(waitingListLimit !=0 && waitingList.size() < waitingListLimit){
            waitingList.add(entrant);
        } else if (waitingListLimit != 0) {
            //notify entrant that waiting list is full

        }
        else {
            waitingList.add(entrant);
        }
    }

    /**
     * removes an entrant from the waitingList or, if already selected will remove the entrant from the selectedList instead
     * @param entrant entrant to be removed
     */
    public void removeEntrant(Entrant entrant) {
        if (waitingList.contains(entrant)) {
            waitingList.remove(entrant);
        } else {
            selectedList.remove(entrant);
        }
    }
}