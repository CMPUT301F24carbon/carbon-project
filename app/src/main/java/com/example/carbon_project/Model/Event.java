package com.example.carbon_project.Model;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private List<Entrant> rejectedList;
    private List<Entrant> waitingList;
    private List<Entrant> selectedList;
    private List<Entrant> canceledList;
    private List<Entrant> enrolledList;

    // Constructor
    public Event(String eventId, String name, String description, String organizerId, int capacity, boolean geolocationRequired, String startDate, String endDate, String eventPosterUrl, String qrCodeUrl, Facility facility) {
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
        this.canceledList = new ArrayList<>();
        this.enrolledList = new ArrayList<>();
    }

    public Event(String eventId, String name, String description, String organizerId, int capacity, List<Entrant> waitingList, List<Entrant> selectedList, List<String> canceledList, List<Entrant> enrolledList, boolean geolocationRequired, String startDate, String endDate, String eventPosterUrl, String qrCodeUrl) {
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
        this.enrolledList = enrolledList;
    }

    public boolean isGeolocationRequired() {
        return geolocationRequired;
    }

    public void setGeolocationRequired(boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    public List<Entrant> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(List<Entrant> enrolledList) {
        this.enrolledList = enrolledList;
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

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

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

    public List<Entrant> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<Entrant> waitingList) {
        this.waitingList = waitingList;
    }

    public List<Entrant> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List<Entrant> selectedList) {
        this.selectedList = selectedList;
    }

    public void setCanceledList(List<Entrant> canceledList) {
        this.canceledList = canceledList;
    }

    // Getters and Setters
    public List<Entrant> getCanceledList() { return canceledList; }

    // Add to canceled list
    public void cancelAttendance(Entrant entrant) {
        if (selectedList.contains(entrant)) {
            selectedList.remove(entrant);
            canceledList.add(entrant);
        }
    }

    public void addToWaitingList(Entrant entrant) {
        waitingList.add(entrant);
    }

    public void drawEntrants() {
        if (waitingList.size() <= capacity) {
            selectedList.addAll(waitingList);
        } else {
            Collections.shuffle(waitingList);
            selectedList = waitingList.subList(0, capacity);
        }
    }

    public int remainingCapacity() {
        return capacity - selectedList.size();
    }

    public boolean isFull() {
        return selectedList.size() >= capacity;
    }

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

    // Method to add an entrant to the waiting list
    public void addEntrantToWaitingList(Entrant entrant) {
        if (waitingList.size() < capacity) {
            waitingList.add(entrant);
        } else {
            Log.d("Event", "Event is at full capacity.");
        }
    }

    public List<Entrant> getSelectedEntrants() {
        return selectedList;
    }

    // Convert Event object to a Map for Firestore storage
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
        map.put("canceledList", canceledList);
        map.put("enrolledList", enrolledList);
        map.put("eventPosterUrl", eventPosterUrl);
        map.put("qrCodeUrl", qrCodeUrl);
        map.put("eventStatus", eventStatus);
        map.put("facility", facility != null ? facility.toMap() : null);

        return map;
    }
}