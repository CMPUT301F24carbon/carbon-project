package com.example.carbon_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Facility class.
 */
public class FacilityTest {

    private Facility facility;

    /**
     * Sets up the test environment by initializing a sample Facility object.
     */
    @Before
    public void setUp() {
        facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
    }

    /**
     * Tests the constructor and getter methods of the Facility class.
     */
    @Test
    public void testFacilityConstructor() {
        // Test the constructor and getter methods
        assertEquals("FAC001", facility.getFacilityId());
        assertEquals("Conference Room", facility.getName());
        assertEquals("New York", facility.getLocation());
        assertEquals(50, facility.getCapacity());
        assertEquals("A large conference room with projectors.", facility.getDescription());
    }

    /**
     * Tests the setName method of the Facility class.
     */
    @Test
    public void testSetName() {
        // Test setter method
        facility.setName("Meeting Room");
        assertEquals("Meeting Room", facility.getName());
    }

    /**
     * Tests the setLocation method of the Facility class.
     */
    @Test
    public void testSetLocation() {
        // Test setter method
        facility.setLocation("Los Angeles");
        assertEquals("Los Angeles", facility.getLocation());
    }

    /**
     * Tests the setCapacity method of the Facility class.
     */
    @Test
    public void testSetCapacity() {
        // Test setter method
        facility.setCapacity(100);
        assertEquals(100, facility.getCapacity());
    }

    /**
     * Tests the setDescription method of the Facility class.
     */
    @Test
    public void testSetDescription() {
        // Test setter method
        facility.setDescription("A small meeting room.");
        assertEquals("A small meeting room.", facility.getDescription());
    }

    /**
     * Tests creating a Facility object with empty fields.
     */
    @Test
    public void testFacilityWithEmptyFields() {
        // Create a facility with empty name
        Facility emptyFacility = new Facility("FAC002", "", "Los Angeles", 0, "");
        assertTrue(emptyFacility.getName().isEmpty());
        assertEquals("Los Angeles", emptyFacility.getLocation());
        assertEquals(0, emptyFacility.getCapacity());
        assertTrue(emptyFacility.getDescription().isEmpty());
    }
}