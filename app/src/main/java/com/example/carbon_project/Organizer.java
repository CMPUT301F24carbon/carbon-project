package com.example.carbon_project;

/**
 * Class represents an Entrant subclass of user.
 * Entrants can join, and leave events and opt-in to receive notifications.
 */
public class Organizer extends User {

    /**
     * Constructs a User object and writes it to firestore
     * @param userId      Unique identifier for the user.
     * @param name        Name of the user.
     * @param email       Email address of the user.
     * @param phoneNumber Phone number of the user.
     * @param profileImage  ProfileImage of the user.
     */
    public Organizer(String userId, String name, String email, String phoneNumber, String profileImage) {
        super(userId, name, email, phoneNumber, profileImage);
    }

    /**
     * Allows the entrant to join an event.
     * @param eventId ID of the event to join.
     */
    public void joinEvent(String eventId) {
    }

    /**
     * Allows the entrant to leave an event.
     * @param eventId ID of the event to leave.
     */
    public void leaveEvent(String eventId) {
    }

    // Entrant notification
    @Override
    public void handleNotification(String message) {
    }

}