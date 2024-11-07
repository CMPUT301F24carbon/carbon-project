package com.example.carbon_project;

/**
 * Class represents an Entrant subclass of user.
 * Entrants can join, and leave events and opt to receive notifications.
 */
public class Entrant extends User {

    /**
     * Constructor for creating an Entrant.
     * @param userId Unique identifier for the user.
     * @param firstName First Name of the user.
     * @param lastName Last Name of the user.
     * @param email Email address of the user.
     * @param phoneNumber Contact number of the user.
     * @param profileImage Contact number of the user.
     */
    public Entrant(String userId, String firstName, String lastName, String email, String phoneNumber, String profileImage) {
        super(userId, firstName, lastName, email, phoneNumber, profileImage);
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