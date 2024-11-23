package com.example.carbon_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Event class.
 */
public class EventTest {

    private Event event;
    private Event eventWithWaitingListLimit;

    /**
     * Sets up the test environment before each test.
     * Initializes Event objects for testing.
     */
    @Before
    public void setUp() {
        event = new Event("E002", "Workshop", "Community Center", 50, false, "2023-11-10", "2023-11-11", 0, null);
        eventWithWaitingListLimit = new Event("E002", "Workshop", "Community Center", 50, false, "2023-11-10", "2023-11-11", 10, null);
    }

    /**
     * Tests the getters for an event created with start and end dates.
     */
    @Test
    public void testEventWithDates() {
        assertEquals("E001", event.getEventId());
        assertEquals("Conference", event.getName());
        assertEquals("City Hall", event.getLocation());
        assertEquals(300, event.getCapacity());
        assertTrue(event.isGeolocationRequired());
        assertEquals("2023-11-01", event.getStartDate());
        assertEquals("2023-11-03", event.getEndDate());
    }

    /**
     * Tests the getters for an event created with a waiting list limit.
     */
    @Test
    public void testEventWithWaitingListLimit() {
        assertEquals("E002", eventWithWaitingListLimit.getEventId());
        assertEquals("Workshop", eventWithWaitingListLimit.getName());
        assertEquals("Community Center", eventWithWaitingListLimit.getLocation());
        assertEquals(50, eventWithWaitingListLimit.getCapacity());
        assertFalse(eventWithWaitingListLimit.isGeolocationRequired());
        assertEquals(10, eventWithWaitingListLimit.getWaitingListLimit());
        assertEquals("2023-11-10", eventWithWaitingListLimit.getStartDate());
        assertEquals("2023-11-11", eventWithWaitingListLimit.getEndDate());
    }

    /**
     * Tests the setter for eventId.
     */
    @Test
    public void testSetEventId() {
        event.setEventId("E003");
        assertEquals("E003", event.getEventId());
    }

    /**
     * Tests the setter for name.
     */
    @Test
    public void testSetName() {
        event.setName("Updated Conference");
        assertEquals("Updated Conference", event.getName());
    }

    /**
     * Tests the setter for location.
     */
    @Test
    public void testSetLocation() {
        event.setLocation("New Venue");
        assertEquals("New Venue", event.getLocation());
    }

    /**
     * Tests the setter for capacity.
     */
    @Test
    public void testSetCapacity() {
        event.setCapacity(500);
        assertEquals(500, event.getCapacity());
    }

    /**
     * Tests the setter for geolocationRequired.
     */
    @Test
    public void testSetGeolocationRequired() {
        event.setGeolocationRequired(false);
        assertFalse(event.isGeolocationRequired());
    }

    /**
     * Tests the setter for startDate.
     */
    @Test
    public void testSetStartDate() {
        event.setStartDate("2023-12-01");
        assertEquals("2023-12-01", event.getStartDate());
    }

    /**
     * Tests the setter for endDate.
     */
    @Test
    public void testSetEndDate() {
        event.setEndDate("2023-12-02");
        assertEquals("2023-12-02", event.getEndDate());
    }

    /**
     * Tests the setter for waitingListLimit.
     */
    @Test
    public void testSetWaitingListLimit() {
        event.setWaitingListLimit(20);
        assertEquals(20, event.getWaitingListLimit());
    }
}