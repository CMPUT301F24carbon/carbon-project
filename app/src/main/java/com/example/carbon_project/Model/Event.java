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

    // Constructor
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

    public boolean isGeolocationRequired() {
        return geolocationRequired;
    }

    public void setGeolocationRequired(boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    public List<String> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(List<String> enrolledList) {
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

    public void setOrganizerId(String facilityId) {
        this.organizerId = organizerId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public void setCanceledList(List<String> canceledList) {
        this.canceledList = canceledList;
    }

    // Getters and Setters
    public List<String> getCanceledList() { return canceledList; }

    // Add to canceled list
    public void cancelAttendance(String entrant) {
        if (selectedList.contains(entrant)) {
            selectedList.remove(entrant);
            canceledList.add(entrant);
        }
    }

    public void addToWaitingList(String entrant) {
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
    public void addEntrantToWaitingList(String entrant) {
        if (waitingList.size() < capacity) {
            waitingList.add(entrant);
        } else {
            Log.d("Event", "Event is at full capacity.");
        }
    }

    public void deleteEvent() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(this.eventId).delete();
    }

    public List<String> getSelectedEntrants() {
        return selectedList;
    }

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