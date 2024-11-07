package com.example.carbon_project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FacilityTest {

    private Facility facility;

    @BeforeEach
    public void setUp() {
        // Initialize a Facility object before each test
        facility = new Facility("F001", "Community Hall", "123 Main St", 100, "A large community hall for events.");
    }

    @Test
    void testGetFacilityId() {
        assertEquals("F001", facility.getFacilityId());
    }

    @Test
    void testGetName() {
        assertEquals("Community Hall", facility.getName());
    }

    @Test
    void testSetName() {
        facility.setName("Updated Hall");
        assertEquals("Updated Hall", facility.getName());
    }

    @Test
    void testGetLocation() {
        assertEquals("123 Main St", facility.getLocation());
    }

    @Test
    void testSetLocation() {
        facility.setLocation("456 Elm St");
        assertEquals("456 Elm St", facility.getLocation());
    }

    @Test
    void testGetCapacity() {
        assertEquals(100, facility.getCapacity());
    }

    @Test
    void testSetCapacity() {
        facility.setCapacity(150);
        assertEquals(150, facility.getCapacity());
    }

    @Test
    void testGetDescription() {
        assertEquals("A large community hall for events.", facility.getDescription());
    }

    @Test
    void testSetDescription() {
        facility.setDescription("Updated description for the community hall.");
        assertEquals("Updated description for the community hall.", facility.getDescription());
    }

    @Test
    void testGetEvents() {
        Event event = new Event("E001", "Annual Meeting", "2025-05-01", "Community Hall", 50, false);
        facility.addEvent(event);

        assertEquals(1, facility.getEvents().size());
        assertEquals("E001", facility.getEvents().get(0).getEventId());
    }

    @Test
    void testAddEvent() {
        Event event1 = new Event("E001", "Community Workshop", "2025-06-01", "Community Hall", 75, false);
        Event event2 = new Event("E002", "Festival", "2025-07-01", "Community Hall", 150, true);

        facility.addEvent(event1);
        facility.addEvent(event2);

        ArrayList<Event> events = facility.getEvents();
        assertEquals(2, events.size());
        assertEquals(event1, events.get(0));
        assertEquals(event2, events.get(1));
    }
}