package com.example.carbon_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Unit tests for the Event class in the Carbon Project.
 * This test class validates the functionality of the Event class,
 * ensuring proper initialization, event operations (waiting list, drawing entrants, etc.),
 * and conversion to a map for database storage.
 */
public class EventTest {

    private Event event;
    private Facility facility;

    /**
     * Setup method to initialize test data before each test.
     * Creates a mock Facility and a sample Event with valid data.
     */
    @Before
    public void setUp() {
        // Create a mock facility
        facility = new Facility("facility1", "Facility One", "1234 Address St", 100, "Main Conference Room");

        // Create an event with real data
        event = new Event(
                "event123", facility.getFacilityId(), "A conference on the latest tech trends.",
                "organizer1", 50, true, "2024-05-01", "2024-05-03",
                "http://example.com/poster.jpg", "http://example.com/qrcode.jpg"
        );
    }

    /**
     * Test the initialization of the Event object to ensure all properties are set correctly.
     */
    @Test
    public void testEventInitialization() {
        // Check if the event is initialized correctly
        assertEquals("event123", event.getEventId());
        assertEquals("Tech Conference 2024", event.getName());
        assertEquals("A conference on the latest tech trends.", event.getDescription());
        assertEquals("facility1", event.getOrganizerId());
        assertEquals(50, event.getCapacity());
        assertTrue(event.isGeolocationRequired());
        assertEquals("2024-05-01", event.getStartDate());
        assertEquals("2024-05-03", event.getEndDate());
        assertEquals("http://example.com/poster.jpg", event.getEventPosterUrl());
        assertEquals("http://example.com/qrcode.jpg", event.getQrCodeUrl());
    }

    /**
     * Test the functionality of adding entrants to the event's waiting list.
     */
    @Test
    public void testAddEntrantToWaitingList() {
        // Add entrants to the waiting list
        event.addEntrantToWaitingList("entrant1");
        event.addEntrantToWaitingList("entrant2");

        // Check if entrants are added to the waiting list
        List<String> waitingList = event.getWaitingList();
        assertEquals(2, waitingList.size());
        assertTrue(waitingList.contains("entrant1"));
        assertTrue(waitingList.contains("entrant2"));
    }

    /**
     * Test the drawing of entrants for the event, ensuring only those selected are moved to the selected list.
     */
    @Test
    public void testDrawEntrants() {
        // Add more entrants than the event capacity
        event.addEntrantToWaitingList("entrant1");
        event.addEntrantToWaitingList("entrant2");
        event.addEntrantToWaitingList("entrant3");
        event.addEntrantToWaitingList("entrant4");
        event.addEntrantToWaitingList("entrant5");

        // Draw entrants based on the event capacity
        event.drawEntrants();

        // Check if only the selected entrants are added to the selected list
        List<String> selectedList = event.getSelectedList();
        assertEquals(5, selectedList.size()); // Event capacity is 50
        assertTrue(selectedList.contains("entrant1"));
        assertTrue(selectedList.contains("entrant2"));
    }

    /**
     * Test the cancellation of an entrant's attendance, ensuring proper removal and addition to the canceled list.
     */
    @Test
    public void testCancelAttendance() {
        // Add entrants to the selected list
        event.getSelectedList().add("entrant1");
        event.getSelectedList().add("entrant2");

        // Cancel attendance for entrant1
        event.cancelAttendance("entrant1");

        // Verify if entrant1 is removed from the selected list and added to canceled list
        assertFalse(event.getSelectedList().contains("entrant1"));
        assertTrue(event.getCanceledList().contains("entrant1"));
    }

    /**
     * Test the calculation of the remaining capacity of the event.
     * The remaining capacity should be the event's total capacity minus the number of selected entrants.
     */
    @Test
    public void testRemainingCapacity() {
        // Add entrants to the selected list
        event.getSelectedList().add("entrant1");
        event.getSelectedList().add("entrant2");

        // Check remaining capacity
        assertEquals(48, event.remainingCapacity()); // Total capacity = 50, 2 selected
    }

    /**
     * Test the functionality of checking if the event is full.
     * The event is considered full if the number of selected entrants equals the event's capacity.
     */
    @Test
    public void testIsFull() {
        // Add entrants until capacity is full
        for (int i = 0; i < 50; i++) {
            event.addEntrantToWaitingList("entrant" + (i + 1));
        }
        event.drawEntrants();

        // Verify if the event is full
        assertTrue(event.isFull());
    }

    /**
     * Test the validity of the event dates, ensuring that the start date is before the end date.
     */
    @Test
    public void testIsValidEventDate() {
        // Check if event start and end dates are valid
        assertTrue(event.isValidEventDate());

        // Set invalid event dates
        event.setStartDate("2024-05-05");
        event.setEndDate("2024-05-03");

        // Verify invalid date scenario
        assertFalse(event.isValidEventDate());
    }

    /**
     * Test the conversion of the event object to a Map representation.
     * This is used for storing the event data in a database or transferring it across systems.
     */
    @Test
    public void testToMapConversion() {
        // Convert the event to a map and check if all fields are correctly mapped
        Map<String, Object> eventMap = event.toMap();
        assertNotNull(eventMap);
        assertEquals("event123", eventMap.get("eventId"));
        assertEquals("Tech Conference 2024", eventMap.get("name"));
        assertEquals(50, eventMap.get("capacity"));
        assertTrue(eventMap.containsKey("waitingList"));
        assertTrue(eventMap.containsKey("selectedList"));
        assertTrue(eventMap.containsKey("notSelectedList"));
        assertTrue(eventMap.containsKey("canceledList"));
        assertTrue(eventMap.containsKey("enrolledList"));
        assertNotNull(eventMap.get("facility"));
    }
}
