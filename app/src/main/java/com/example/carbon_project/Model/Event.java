package com.example.carbon_project.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an event in the system, which is associated with a facility and can have multiple users
 * enrolled in it. The event contains various attributes like its name, description, capacity, start
 * and end dates, and lists of users who have been selected, waiting, not selected, canceled, or enrolled.
 */
public class Event {

    private String eventId;
    private String name;
    private String description;
    private String organizerId;
    private int capacity;
    private boolean geolocationRequired;
    private String startDate;
    private String endDate;
    private String eventPosterUrl;
    private String qrCodeUrl;
    private String eventStatus;
    private Facility facility;
    private List<String> notSelectedList;
    private List<String> waitingList;
    private List<String> selectedList;
    private List<String> canceledList;
    private List<String> enrolledList;

    /**
     * Constructor to initialize an event with the provided details.
     * 
     * @param eventId          The unique identifier for the event.
     * @param name            The name of the event.
     * @param description     A brief description of the event.
     * @param organizerId     The ID of the event organizer.
     * @param capacity        The maximum number of users that can enroll in the event.
     * @param geolocationRequired A flag to indicate if geolocation is required for the event.
     * @param startDate       The start date of the event.
     * @param endDate         The end date of the event.
     * @param eventPosterUrl  URL for the event's poster image.
     * @param qrCodeUrl       URL for the event's QR code.
     * @param facility        The facility hosting the event.
     */
    public Event(String eventId, String name, String description, String organizerId, int capacity, boolean geolocationRequired, 
                 String startDate, String endDate, String eventPosterUrl, String qrCodeUrl, Facility facility) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.organizerId = organizerId;
        this.capacity = capacity;
        this.geolocationRequired = geolocationRequired;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventPosterUrl = eventPosterUrl;
        this.qrCodeUrl = qrCodeUrl;
        this.facility = facility;
        this.waitingList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.notSelectedList = new ArrayList<>();
        this.canceledList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
    }

    /**
     * Constructor to initialize an event with specific lists for attendees.
     * 
     * @param eventId          The unique identifier for the event.
     * @param name            The name of the event.
     * @param description     A brief description of the event.
     * @param organizerId     The ID of the event organizer.
     * @param capacity        The maximum number of users that can enroll in the event.
     * @param waitingList     The list of users who are currently waiting for the event.
     * @param selectedList    The list of users who have been selected for the event.
     * @param canceledList    The list of users who canceled their attendance.
     * @param enrolledList    The list of users who have enrolled for the event.
     * @param geolocationRequired A flag to indicate if geolocation is required for the event.
     * @param startDate       The start date of the event.
     * @param endDate         The end date of the event.
     * @param eventPosterUrl  URL for the event's poster image.
     * @param qrCodeUrl       URL for the event's QR code.
     */
    public Event(String eventId, String name, String description, String organizerId, int capacity, List<String> waitingList, 
                 List<String> selectedList, List<String> canceledList, List<String> enrolledList, boolean geolocationRequired, 
                 String startDate, String endDate, String eventPosterUrl, String qrCodeUrl) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.organizerId = organizerId;
        this.capacity = capacity;
        this.geolocationRequired = geolocationRequired;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventPosterUrl = eventPosterUrl;
        this.qrCodeUrl = qrCodeUrl;
        this.waitingList = waitingList;
        this.selectedList = selectedList;
        this.notSelectedList = selectedList;
        this.enrolledList = enrolledList;
    }

    // Getters and Setters for event properties

    /**
     * Returns the event's ID.
     * 
     * @return The event's unique identifier.
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isGeolocationRequired() {
        return geolocationRequired;
    }

    public void setGeolocationRequired(boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventPosterUrl() {
        return eventPosterUrl;
    }

    public void setEventPosterUrl(String eventPosterUrl) {
        this.eventPosterUrl = eventPosterUrl;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<String> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<String> waitingList) {
        this.waitingList = waitingList;
    }

    public List<String> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List<String> selectedList) {
        this.selectedList = selectedList;
    }

    public List<String> getNotSelectedList() {
        return notSelectedList;
    }

    public void setNotSelectedList(List<String> notSelectedList) {
        this.notSelectedList = notSelectedList;
    }

    public List<String> getCanceledList() {
        return canceledList;
    }

    public void setCanceledList(List<String> canceledList) {
        this.canceledList = canceledList;
    }

    public List<String> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(List<String> enrolledList) {
        this.enrolledList = enrolledList;
    }

    // Event-related methods

    /**
     * Cancels an entrant's attendance by moving them from the selected list to the canceled list.
     * 
     * @param entrant The entrant's ID.
     */
    public void cancelAttendance(String entrant) {
        if (selectedList.contains(entrant)) {
            selectedList.remove(entrant);
            canceledList.add(entrant);
        }
    }

    /**
     * Adds an entrant to the waiting list.
     * 
     * @param entrant The entrant's ID.
     */
    public void addToWaitingList(String entrant) {
        waitingList.add(entrant);
    }

    /**
     * Draws entrants from the waiting list to be selected based on the event's capacity.
     * If the number of entrants on the waiting list exceeds capacity, a random selection is made.
     */
    public void drawEntrants() {
        if (waitingList.size() <= capacity) {
            selectedList.addAll(waitingList);
        } else {
            Collections.shuffle(waitingList);
            selectedList = waitingList.subList(0, capacity);
        }
    }

    /**
     * Returns the remaining capacity for the event.
     * 
     * @return The number of remaining slots for the event.
     */
    public int remainingCapacity() {
        return capacity - selectedList.size();
    }

    /**
     * Checks whether the event has reached its full capacity.
     * 
     * @return True if the event is full, false otherwise.
     */
    public boolean isFull() {
        return selectedList.size() >= capacity;
    }

    /**
     * Validates whether the event's start and end dates are in a valid range.
     * 
     * @return True if the start date is before the end date, false otherwise.
     */
    public boolean isValidEventDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return !start.after(end);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Adds an entrant to the waiting list if the event's capacity is not yet full.
     * 
     * @param entrant The entrant's ID.
     */
    public void addEntrantToWaitingList(String entrant) {
        if (waitingList.size() < capacity) {
            waitingList.add(entrant);
        } else {
            Log.d("Event", "Event is at full capacity.");
        }
    }

    /**
     * Converts the Event object into a Map suitable for Firestore storage.
     * 
     * @return A map containing all event details.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("eventId", eventId);
        map.put("name", name);
        map.put("description", description);
        map.put("organizerId", organizerId);
        map.put("capacity", capacity);
        map.put("geolocationRequired", geolocationRequired);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("waitingList", waitingList);
        map.put("selectedList", selectedList);
        map.put("notSelectedList", notSelectedList);
        map.put("canceledList", canceledList);
        map.put("enrolledList", enrolledList);
        map.put("eventPosterUrl", eventPosterUrl);
        map.put("qrCodeUrl", qrCodeUrl);
        map.put("eventStatus", eventStatus);
        map.put("facility", facility != null ? facility.toMap() : null);
        return map;
    }
}
