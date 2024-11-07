package com.example.carbon_project;

/**
 * Class represents an Admin subclass of User.
 * Admins has the ability to delete events and remove users.
 */
public class Admin extends User {

    /**
     * Constructor for creating an Admin.
     * @param userId Unique identifier for the user.
     * @param firstName First Name of the user.
     * @param lastName Last Name of the user.
     * @param email Email address of the user.
     * @param phoneNumber Contact number of the user.
     * @param profileImage Contact number of the user.
     */
    public Admin(String userId, String firstName, String lastName, String email, String phoneNumber, String profileImage) {
        super(userId, firstName, lastName, email, phoneNumber, profileImage);
    }

    /**
     * Allows the admin to delete an event.
     * @param eventId ID of the event to delete.
     */
    public void deleteEvent(String eventId) {
    }

    /**
     * Allows the admin to delete a user.
     * @param userId ID of the user to remove.
     */
    public void removeUser(String userId) {
    }

    // Admin notification
    @Override
    public void handleNotification(String message) {
    }
}