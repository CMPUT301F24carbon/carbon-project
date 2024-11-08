package com.example.carbon_project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.carbon_project.Facility;

public class FacilityTest {

    private Facility facility;

    @Before
    public void setUp() {
        // Initialize a sample facility before each test
        facility = new Facility("FAC001", "Conference Room", "New York", 50, "A large conference room with projectors.");
    }

    @Test
    public void testFacilityConstructor() {
        // Test the constructor and getter methods
        assertEquals("FAC001", facility.getFacilityId());
        assertEquals("Conference Room", facility.getName());
        assertEquals("New York", facility.getLocation());
        assertEquals(50, facility.getCapacity());
        assertEquals("A large conference room with projectors.", facility.getDescription());
    }

    @Test
    public void testSetName() {
        // Test setter method
        facility.setName("Meeting Room");
        assertEquals("Meeting Room", facility.getName());
    }

    @Test
    public void testSetLocation() {
        // Test setter method
        facility.setLocation("Los Angeles");
        assertEquals("Los Angeles", facility.getLocation());
    }

    @Test
    public void testSetCapacity() {
        // Test setter method
        facility.setCapacity(100);
        assertEquals(100, facility.getCapacity());
    }

    @Test
    public void testSetDescription() {
        // Test setter method
        facility.setDescription("A small meeting room.");
        assertEquals("A small meeting room.", facility.getDescription());
    }

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