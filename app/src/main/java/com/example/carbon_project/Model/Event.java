package com.example.carbon_project.Model;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Represents an event with various details such as name, description, capacity, and lists of entrants.
 * The Event class provides functionality for managing entrants, handling event status, capacity checks,
 * and saving or deleting events from Firestore.
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

    private List<String> waitingList;
    private List<String> selectedList;
    private List<String> canceledList;
    private List<String> enrolledList;

    /**
     * Constructs an Event instance using a map of event data.
     * The map should contain keys like "eventId", "name", "description", "organizerId", "capacity",
     * "geolocationRequired", "startDate", "endDate", and optionally "waitingList", "selectedList", "canceledList", and "enrolledList".
     *
     * @param dataMap A map containing event attributes.
     */
    public Event(Map<String, Object> dataMap) {
        this.eventId = (String) dataMap.get("eventId");
        this.name = (String) dataMap.get("name");
        this.description = (String) dataMap.get("description");
        this.organizerId = (String) dataMap.get("organizerId");
        Long longCapacity = (Long) dataMap.get("capacity");
        this.capacity = longCapacity != null ? longCapacity.intValue() : 0;
        this.geolocationRequired = (boolean) dataMap.get("geolocationRequired");
        this.startDate = (String) dataMap.get("startDate");
        this.endDate = (String) dataMap.get("endDate");

        if (dataMap.containsKey("waitingList")) {
            this.waitingList = (List<String>) dataMap.get("waitingList");
        } else {
            this.waitingList = new ArrayList<>();
        }
        if (dataMap.containsKey("selectedList")) {
            this.selectedList = (List<String>) dataMap.get("selectedList");
        } else {
            this.selectedList = new ArrayList<>();
        }
        if (dataMap.containsKey("canceledList")) {
            this.canceledList = (List<String>) dataMap.get("canceledList");
        } else {
            this.canceledList = new ArrayList<>();
        }
        if (dataMap.containsKey("enrolledList")) {
            this.enrolledList = (List<String>) dataMap.get("enrolledList");
        } else {
            this.enrolledList = new ArrayList<>();
        }

        this.eventPosterUrl = (String) dataMap.get("eventPosterUrl");
        this.qrCodeUrl = (String) dataMap.get("qrCodeUrl");
    }
    /**
     * Constructs an Event instance with individual attributes.
     * Initializes empty lists for waiting list, selected list, canceled list, and enrolled list.
     *
     * @param eventId             The unique ID of the event.
     * @param name               The name of the event.
     * @param description        A description of the event.
     * @param organizerId        The organizer's ID.
     * @param capacity           The maximum number of participants.
     * @param geolocationRequired Whether the event requires geolocation.
     * @param startDate          The start date of the event.
     * @param endDate            The end date of the event.
     * @param eventPosterUrl     The URL of the event's poster.
     * @param qrCodeUrl          The URL of the event's QR code.
     */
    public Event(String eventId, String name, String description, String organizerId, int capacity, boolean geolocationRequired, String startDate, String endDate, String eventPosterUrl, String qrCodeUrl) {
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
        this.waitingList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.canceledList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
    }

    /**
     * Constructs an Event instance with all attributes specified.
     *
     * @param eventId            The unique ID of the event.
     * @param name               The name of the event.
     * @param description        A description of the event.
     * @param organizerId        The organizer's ID.
     * @param capacity           The maximum number of participants.
     * @param waitingList        The list of entrants on the waiting list.
     * @param selectedList       The list of selected entrants.
     * @param canceledList       The list of entrants who canceled.
     * @param enrolledList       The list of enrolled entrants.
     * @param geolocationRequired Whether the event requires geolocation.
     * @param startDate          The start date of the event.
     * @param endDate            The end date of the event.
     * @param eventPosterUrl     The URL of the event's poster.
     * @param qrCodeUrl          The URL of the event's QR code.
     */

    public Event(String eventId, String name, String description, String organizerId, int capacity, List<String> waitingList, List<String> selectedList, List<String> canceledList, List<String> enrolledList, boolean geolocationRequired, String startDate, String endDate, String eventPosterUrl, String qrCodeUrl) {
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
        this.canceledList = canceledList;
        this.enrolledList = enrolledList;
    }

    /**
     * Checks if geolocation is required for this event.
     *
     * @return {@code true} if geolocation is required, otherwise {@code false}.
     */
    public boolean isGeolocationRequired() {
        return geolocationRequired;
    }

    /**
     * Sets whether geolocation is required for this event.
     *
     * @param geolocationRequired {@code true} if geolocation is required, otherwise {@code false}.
     */
    public void setGeolocationRequired(boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    /**
     * Returns the list of entrants who are enrolled in the event.
     *
     * @return A list of user IDs of enrolled entrants.
     */
    public List<String> getEnrolledList() {
        return enrolledList;
    }

    /**
     * Sets the list of entrants who are enrolled in the event.
     *
     * @param enrolledList A list of user IDs of enrolled entrants.
     */
    public void setEnrolledList(List<String> enrolledList) {
        this.enrolledList = enrolledList;
    }

    /**
     * Returns the event start date.
     *
     * @return The start date of the event as a string.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the event start date.
     *
     * @param startDate The start date of the event as a string.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    /**
     * Returns the event end date.
     *
     * @return The end date of the event as a string.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Returns the URL of the event's poster.
     *
     * @return The event poster URL.
     */
    public String getEventPosterUrl() {
        return eventPosterUrl;
    }

    /**
     * Sets the URL of the event's poster.
     *
     * @param eventPosterUrl The event poster URL.
     */
    public void setEventPosterUrl(String eventPosterUrl) {
        this.eventPosterUrl = eventPosterUrl;
    }

    /**
     * Returns the URL of the event's QR code.
     *
     * @return The QR code URL.
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * Sets the URL of the event's QR code.
     *
     * @param qrCodeUrl The QR code URL.
     */
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    /**
     * Returns the status of the event.
     *
     * @return The event status.
     */
    public String getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets the status of the event.
     *
     * @param eventStatus The event status.
     */
    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Sets the event end date.
     *
     * @param endDate The end date of the event as a string.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the unique ID of the event.
     *
     * @return The event's unique ID.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the unique ID of the event.
     *
     * @param eventId The event's unique ID.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the name of the event.
     *
     * @return The event's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name The event's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the event.
     *
     * @return The event's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the event.
     *
     * @param description The event's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the organizer's ID for the event.
     *
     * @return The organizer's ID.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer's ID for the event.
     *
     * @param organizerId The organizer's ID.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Gets the capacity of the event (the maximum number of participants).
     *
     * @return The event's capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the event (the maximum number of participants).
     *
     * @param capacity The event's capacity.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the list of participants on the event's waiting list.
     *
     * @return A list of user IDs of entrants on the waiting list.
     */
    public List<String> getWaitingList() {
        return waitingList;
    }

    /**
     * Sets the list of participants on the event's waiting list.
     *
     * @param waitingList A list of user IDs of entrants on the waiting list.
     */
    public void setWaitingList(List<String> waitingList) {
        this.waitingList = waitingList;
    }

    /**
     * Gets the list of selected participants for the event.
     *
     * @return A list of user IDs of selected participants.
     */
    public List<String> getSelectedList() {
        return selectedList;
    }

    /**
     * Sets the list of selected participants for the event.
     *
     * @param selectedList A list of user IDs of selected participants.
     */
    public void setSelectedList(List<String> selectedList) {
        this.selectedList = selectedList;
    }

    /**
     * Sets the list of canceled participants for the event.
     *
     * @param canceledList A list of user IDs of canceled participants.
     */
    public void setCanceledList(List<String> canceledList) {
        this.canceledList = canceledList;
    }

    /**
     * Gets the list of participants who have canceled their attendance for the event.
     *
     * @return A list of user IDs of participants who canceled.
     */
    // Getters and Setters
    public List<String> getCanceledList() { return canceledList; }

    /**
     * Cancels the attendance of a participant. If the participant is in the selected list,
     * they will be moved to the canceled list and removed from the selected list.
     *
     * @param entrant The user ID of the entrant whose attendance is being canceled.
     */
    // Add to canceled list
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
     * Draws entrants from the waiting list to fill the selected list based on the event's capacity.
     * If the waiting list size exceeds capacity, only a random subset of entrants will be selected.
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
     * Returns the remaining capacity of the event.
     *
     * @return The remaining capacity as an integer.
     */
    public int remainingCapacity() {
        return capacity - selectedList.size();
    }

    /**
     * Checks if the event is full.
     *
     * @return {@code true} if the event is full, otherwise {@code false}.
     */
    public boolean isFull() {
        return selectedList.size() >= capacity;
    }

    /**
     * Validates that the event's start date is before the end date.
     *
     * @return {@code true} if the event dates are valid, otherwise {@code false}.
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
     * Adds an entrant to the waiting list.
     *
     * @param entrant The entrant's ID.
     */
    // Method to add an entrant to the waiting list
    public void addEntrantToWaitingList(String entrant) {
        if (waitingList.size() < capacity) {
            waitingList.add(entrant);
        } else {
            Log.d("Event", "Event is at full capacity.");
        }
    }

    /**
     * Deletes the event from Firestore.
     */
    public void deleteEvent() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(this.eventId).delete();
    }


    public List<String> getSelectedEntrants() {
        return selectedList;
    }

    /**
     * Returns a map representation of the Event object for Firestore storage.
     *
     * @return A map containing the event's attributes.
     */
    // Convert Event object to a Map for Firestore storage
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("eventId", eventId);
        map.put("name", name != null ? name : "");
        map.put("description", description != null ? description : "");
        map.put("organizerId", organizerId != null ? organizerId : "");
        map.put("capacity", capacity);
        map.put("geolocationRequired", geolocationRequired);
        map.put("startDate", startDate != null ? startDate : "");
        map.put("endDate", endDate != null ? endDate : "");
        map.put("waitingList", waitingList);
        map.put("selectedList", selectedList);
        map.put("canceledList", canceledList);
        map.put("enrolledList", enrolledList);
        map.put("eventPosterUrl", eventPosterUrl);
        map.put("qrCodeUrl", qrCodeUrl);

        return map;
    }

    /**
     * Retrieves the current status of a specific entrant.
     *
     * @param userId The user ID of the entrant.
     * @return The status of the entrant in relation to the event (e.g., "Selected", "Waitlist", "Not Selected").
     */
    // Method to get the status of an entrant
    public String getStatus(String userId) {
        String status;
        if (getWaitingList().contains(userId) && getSelectedList() != null) {
            status = "Waitlist";
        } else if (getWaitingList().contains(userId)) {
            status = "Not Selected";
        } else if (getSelectedList().contains(userId)) {
            status = "Selected";
        } else if (getEnrolledList().contains(userId)) {
            status = "Enrolled";
        } else {
            status = "Joinable";
        }
        return status;
    }
}