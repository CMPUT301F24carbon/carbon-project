package com.example.carbon_project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Event class, covering constructors, getters, setters,
 * and event management methods like adding and removing entrants and running a lottery.
 */
public class EventTest {

    /**
     * Tests the constructor for creating an Event without a waiting list limit,
     * verifying that all fields are set correctly.
     */
    @Test
    public void testConstructorWithoutWaitingListLimit() {
        Event eventWithoutLimit = new Event("E2", "No Limit Event", "2024-12-01", "No Limit Location", 15, false);
        assertEquals("E2", eventWithoutLimit.getEventId());
        assertEquals("No Limit Event", eventWithoutLimit.getName());
        assertEquals("2024-12-01", eventWithoutLimit.getDate());
        assertEquals("No Limit Location", eventWithoutLimit.getLocation());
        assertEquals(15, eventWithoutLimit.getCapacity());
        assertFalse(eventWithoutLimit.isGeolocationRequired());
    }

    /**
     * Tests the constructor for creating an Event with a waiting list limit,
     * verifying that all fields are set correctly, including the waiting list limit.
     */
    @Test
    public void testConstructorWithWaitingListLimit() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 5);
        assertEquals("E1", event.getEventId());
        assertEquals("Sample Event", event.getName());
        assertEquals("2024-11-08", event.getDate());
        assertEquals("Sample Location", event.getLocation());
        assertEquals(10, event.getCapacity());
        assertTrue(event.isGeolocationRequired());
        assertEquals(5, event.getWaitingListLimit());
    }

    /**
     * Tests adding an entrant to the event's waiting list within the limit,
     * ensuring the entrant is added successfully.
     */
    @Test
    public void testAddEntrantWithinLimit() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 5);
        Entrant entrant1 = new Entrant("Entrant1", "Alice", "Smith", "alice@example.com", "1234567890", "profileImage1.jpg");

        event.addEntrant(entrant1);
        assertEquals(1, event.getWaitingList().size());
        assertTrue(event.getWaitingList().contains(entrant1));
    }

    /**
     * Tests adding entrants beyond the waiting list limit, ensuring that
     * additional entrants are not added once the limit is reached.
     */
    @Test
    public void testAddEntrantExceedingLimit() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 5);

        for (int i = 0; i < 5; i++) {
            event.addEntrant(new Entrant("Entrant" + i, "First" + i, "Last" + i, "email" + i + "@example.com", "12345" + i, "profileImage" + i + ".jpg"));
        }
        Entrant entrant1 = new Entrant("Entrant6", "Alice", "Smith", "alice@example.com", "1234567890", "profileImage1.jpg");
        event.addEntrant(entrant1);  // Should not be added since limit is reached
        assertEquals(5, event.getWaitingList().size());
        assertFalse(event.getWaitingList().contains(entrant1));
    }

    /**
     * Tests removing an entrant from the waiting list, verifying that
     * the entrant is removed successfully and the list size is updated.
     */
    @Test
    public void testRemoveEntrantFromWaitingList() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 5);
        Entrant entrant1 = new Entrant("Entrant1", "Alice", "Smith", "alice@example.com", "1234567890", "profileImage1.jpg");

        event.addEntrant(entrant1);
        event.removeEntrant(entrant1);
        assertEquals(0, event.getWaitingList().size());
        assertFalse(event.getWaitingList().contains(entrant1));
    }

    /**
     * Tests removing an entrant from the selected list, verifying that
     * the entrant is removed if present in the selected list.
     */
    @Test
    public void testRemoveEntrantFromSelectedList() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 5);
        Entrant entrant1 = new Entrant("Entrant1", "Alice", "Smith", "alice@example.com", "1234567890", "profileImage1.jpg");

        event.getSelectedList().add(entrant1);  // Manually add to selected list for testing
        event.removeEntrant(entrant1);
        assertEquals(0, event.getSelectedList().size());
        assertFalse(event.getSelectedList().contains(entrant1));
    }

    /**
     * Tests the lottery selection process, ensuring that entrants are randomly selected
     * from the waiting list to fill the event's capacity and removed from the waiting list.
     */
    @Test
    public void testLotterySelection() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 10);

        for (int i = 0; i < 10; i++) {
            event.addEntrant(new Entrant("Entrant" + i, "First" + i, "Last" + i, "email" + i + "@example.com", "12345" + i, "profileImage" + i + ".jpg"));
        }
        event.lottery();
        assertEquals(event.getCapacity(), event.getSelectedList().size());
        assertEquals(0, event.getWaitingList().size());  // All entrants should be selected or removed
    }

    /**
     * Tests the getters and setters of the Event class to ensure that
     * each field can be set and retrieved correctly.
     */
    @Test
    public void testSettersAndGetters() {
        Event event = new Event("E1", "Sample Event", "2024-11-08", "Sample Location", 10, true, 5);

        event.setEventId("E2");
        event.setName("New Event Name");
        event.setDate("2024-12-01");
        event.setLocation("New Location");
        event.setCapacity(20);
        event.setGeolocationRequired(false);
        event.setWaitingListLimit(3);

        assertEquals("E2", event.getEventId());
        assertEquals("New Event Name", event.getName());
        assertEquals("2024-12-01", event.getDate());
        assertEquals("New Location", event.getLocation());
        assertEquals(20, event.getCapacity());
        assertFalse(event.isGeolocationRequired());
        assertEquals(3, event.getWaitingListLimit());
    }

    /**
     * Tests the constructor and getter methods to ensure that
     * the Facility object is created with the correct values.
     */
    @Test
    public void testFacilityConstructor() {
        Facility facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
        assertEquals("FAC001", facility.getFacilityId());
        assertEquals("Conference Room", facility.getName());
        assertEquals("New York", facility.getLocation());
        assertEquals(50, facility.getCapacity());
        assertEquals("A large conference room with projectors.", facility.getDescription());
    }

    /**
     * Tests the setName method to ensure the Facility name can be updated.
     */
    @Test
    public void testSetName() {
        Facility facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
        facility.setName("Meeting Room");
        assertEquals("Meeting Room", facility.getName());
    }

    /**
     * Tests the setLocation method to ensure the Facility location can be updated.
     */
    @Test
    public void testSetLocation() {
        Facility facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
        facility.setLocation("Los Angeles");
        assertEquals("Los Angeles", facility.getLocation());
    }

    /**
     * Tests the setCapacity method to ensure the Facility capacity can be updated.
     */
    @Test
    public void testSetCapacity() {
        Facility facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
        facility.setCapacity(100);
        assertEquals(100, facility.getCapacity());
    }

    /**
     * Tests the setDescription method to ensure the Facility description can be updated.
     */
    @Test
    public void testSetDescription() {
        Facility facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
        facility.setDescription("A small meeting room.");
        assertEquals("A small meeting room.", facility.getDescription());
    }

    /**
     * Tests creating a Facility with empty name and description fields to ensure
     * fields can be empty if necessary.
     */
    @Test
    public void testFacilityWithEmptyFields() {
        Facility emptyFacility = new Facility("FAC002", "", "Los Angeles", 0, "");
        assertTrue(emptyFacility.getName().isEmpty());
        assertEquals("Los Angeles", emptyFacility.getLocation());
        assertEquals(0, emptyFacility.getCapacity());
        assertTrue(emptyFacility.getDescription().isEmpty());
    }
}