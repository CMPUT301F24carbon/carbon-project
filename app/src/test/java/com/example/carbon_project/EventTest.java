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

    private Event eventWithDates;
    private Event eventWithWaitingListLimit;

    /**
     * Sets up the test environment before each test.
     * Initializes Event objects for testing.
     */
    @Before
    public void setUp() {
        eventWithDates = new Event("E001", "Conference", "City Hall", 300, true, "2023-11-01", "2023-11-03");
        eventWithWaitingListLimit = new Event("E002", "Workshop", "Community Center", 50, false, 10, "2023-11-10", "2023-11-11");
    }

    /**
     * Tests the getters for an event created with start and end dates.
     */
    @Test
    public void testEventWithDates() {
        assertEquals("E001", eventWithDates.getEventId());
        assertEquals("Conference", eventWithDates.getName());
        assertEquals("City Hall", eventWithDates.getLocation());
        assertEquals(300, eventWithDates.getCapacity());
        assertTrue(eventWithDates.isGeolocationRequired());
        assertEquals("2023-11-01", eventWithDates.getStartDate());
        assertEquals("2023-11-03", eventWithDates.getEndDate());
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
        eventWithDates.setEventId("E003");
        assertEquals("E003", eventWithDates.getEventId());
    }

    /**
     * Tests the setter for name.
     */
    @Test
    public void testSetName() {
        eventWithDates.setName("Updated Conference");
        assertEquals("Updated Conference", eventWithDates.getName());
    }

    /**
     * Tests the setter for location.
     */
    @Test
    public void testSetLocation() {
        eventWithDates.setLocation("New Venue");
        assertEquals("New Venue", eventWithDates.getLocation());
    }

    /**
     * Tests the setter for capacity.
     */
    @Test
    public void testSetCapacity() {
        eventWithDates.setCapacity(500);
        assertEquals(500, eventWithDates.getCapacity());
    }

    /**
     * Tests the setter for geolocationRequired.
     */
    @Test
    public void testSetGeolocationRequired() {
        eventWithDates.setGeolocationRequired(false);
        assertFalse(eventWithDates.isGeolocationRequired());
    }

    /**
     * Tests the setter for startDate.
     */
    @Test
    public void testSetStartDate() {
        eventWithDates.setStartDate("2023-12-01");
        assertEquals("2023-12-01", eventWithDates.getStartDate());
    }

    /**
     * Tests the setter for endDate.
     */
    @Test
    public void testSetEndDate() {
        eventWithDates.setEndDate("2023-12-02");
        assertEquals("2023-12-02", eventWithDates.getEndDate());
    }

    /**
     * Tests the setter for waitingListLimit.
     */
    @Test
    public void testSetWaitingListLimit() {
        eventWithDates.setWaitingListLimit(20);
        assertEquals(20, eventWithDates.getWaitingListLimit());
    }
}