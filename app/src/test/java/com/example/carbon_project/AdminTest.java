package com.example.carbon_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.carbon_project.Model.Admin;
import com.example.carbon_project.Model.User;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AdminTest {

    private User user;  // Change to User type since Admin extends User

    // Test data
    private final String userId = "admin1";
    private final String name = "Admin User";
    private final String email = "admin@example.com";
    private final String phoneNumber = "123-456-7890";

    @Before
    public void setUp() {
        // Set up the admin using the constructor that accepts a Map
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("role", "admin");

        user = new Admin(map);  // Admin is assigned to a User variable
    }

    @Test
    public void testConstructorWithMap() {
        // Test the constructor that accepts a Map

        // Ensure the "role" is set to "admin"
        assertEquals("admin", user.getRole());  // Using User reference

        // Verify fields are initialized correctly from the map
        assertEquals(userId, user.getUserId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(phoneNumber, user.getPhoneNumber());
    }

    @Test
    public void testIsAdminRole() {
        // Ensure that the role is set correctly for Admin
        assertTrue(user.isAdmin());   // Should return true as this is an Admin (using User reference)
        assertFalse(user.isOrganizer()); // Should return false for Organizer role
        assertFalse(user.isEntrant());   // Should return false for Entrant role
    }

    @Test
    public void testEmptyAdmin() {
        // Create an Admin object with no additional fields (other than mandatory ones)
        Map<String, Object> emptyMap = new HashMap<>();
        emptyMap.put("userId", "admin2");
        emptyMap.put("name", "Admin Two");
        emptyMap.put("email", "admin2@example.com");
        emptyMap.put("phoneNumber", "987-654-3210");

        User emptyUser = new Admin(emptyMap);  // Treating Admin as User

        // Verify that the fields are set correctly and role is "admin"
        assertEquals("admin2", emptyUser.getUserId());
        assertEquals("Admin Two", emptyUser.getName());
        assertEquals("admin2@example.com", emptyUser.getEmail());
        assertEquals("987-654-3210", emptyUser.getPhoneNumber());
        assertEquals("admin", emptyUser.getRole());  // Ensure role is "admin"
    }

    @Test
    public void testToMap() {
        // Test the toMap method
        Map<String, Object> map = user.toMap();  // Using User reference

        // Verify that the map contains all necessary fields
        assertNotNull(map);
        assertEquals(userId, map.get("userId"));
        assertEquals(name, map.get("name"));
        assertEquals(email, map.get("email"));
        assertEquals(phoneNumber, map.get("phoneNumber"));
        assertEquals("admin", map.get("role"));
    }
}