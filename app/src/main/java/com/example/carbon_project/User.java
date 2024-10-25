package com.example.carbon_project;

/**
 * Represents a user which is the superclass to Entrant, Admin and Organizer.
 */
public abstract class User {
    protected String userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected String profileImage;

    /**
     * Constructs a User object.
     * @param userId      Unique identifier for the user.
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email address of the user.
     * @param phoneNumber Phone number of the user.
     * @param profileImage  ProfileImage of the user.
     */
    public User(String userId, String firstName, String lastName, String email, String phoneNumber, String profileImage) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }

    public String getUserId() { return userId; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() { return email; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getProfileImageUrl() { return profileImage; }

    public void setProfileImageUrl(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * Get the users initials.
     * @return Users initials in the format "FL".
     */
    public String getInitials() {
        return firstName.charAt(0) + "" + lastName.charAt(0);
    }

    public abstract void handleNotification(String message);
}