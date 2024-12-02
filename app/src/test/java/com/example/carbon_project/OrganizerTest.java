package com.example.carbon_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.carbon_project.Model.Organizer;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizerTest {

    private Organizer organizer;

    // Test data
    private final String userId = "user1";
    private final String name = "Organizer One";
    private final String email = "organizer1@example.com";
    private final String phoneNumber = "123-456-7890";
    private final List<String> createdEvents = Arrays.asList("event1", "event2");
    private final List<String> facilityIds = Arrays.asList("facility1", "facility2");

    @Before
    public void setUp() {
        // Set up the organizer using the constructor that accepts a Map
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("createdEvents", createdEvents);
        map.put("facilityIds", facilityIds);

        organizer = new Organizer(map);
    }

    @Test
    public void testConstructorWithMap() {
        // Test the constructor that accepts a Map

        // Ensure the "role" is set to "organizer"
        assertEquals("organizer", organizer.getRole());

        // Verify fields are initialized correctly from the map
        assertEquals(userId, organizer.getUserId());
        assertEquals(name, organizer.getName());
        assertEquals(email, organizer.getEmail());
        assertEquals(phoneNumber, organizer.getPhoneNumber());
        assertEquals(createdEvents, organizer.getCreatedEvents());
        assertEquals(facilityIds, organizer.getFacilityIds());
    }

    @Test
    public void testIsOrganizerRole() {
        // Ensure that the role is set correctly for an Organizer
        assertTrue(organizer.isOrganizer());  // Should return true as this is an Organizer
        assertFalse(organizer.isAdmin());     // Should return false for Admin role
        assertFalse(organizer.isEntrant());   // Should return false for Entrant role
    }

    @Test
    public void testEmptyCreatedEventsAndFacilityIds() {
        // Create an Organizer object with no events or facilities
        Map<String, Object> emptyMap = new HashMap<>();
        emptyMap.put("userId", "user2");
        emptyMap.put("name", "Organizer Two");
        emptyMap.put("email", "organizer2@example.com");
        emptyMap.put("phoneNumber", "987-654-3210");

        Organizer emptyOrganizer = new Organizer(emptyMap);

        // Verify that both lists are empty
        assertTrue(emptyOrganizer.getCreatedEvents().isEmpty());
        assertTrue(emptyOrganizer.getFacilityIds().isEmpty());
    }
}