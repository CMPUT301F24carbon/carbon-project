package com.example.carbon_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.carbon_project.Model.Facility;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Unit tests for the Facility class in the Carbon Project.
 * This test class validates the functionality of the Facility class,
 * ensuring proper initialization and conversion to a map for database storage.
 */
public class FacilityTest {

    private Facility facility;

    /**
     * Setup method to initialize test data before each test.
     * Creates a sample Facility with valid data.
     */
    @Before
    public void setUp() {
        facility = new Facility("facility1", "Facility One", "1234 Address St", 100, "organizer1");
    }

    /**
     * Test the initialization of the Facility object to ensure all properties are set correctly.
     */
    @Test
    public void testFacilityInitialization() {
        // Check if the facility is initialized correctly
        assertEquals("facility1", facility.getFacilityId());
        assertEquals("Facility One", facility.getName());
        assertEquals("1234 Address St", facility.getLocation());
        assertEquals(100, facility.getCapacity());
        assertEquals("organizer1", facility.getOrganizerId());
    }

    /**
     * Test the default constructor of the Facility class.
     * Ensures that the fields are initialized to their default values.
     */
    @Test
    public void testDefaultConstructor() {
        Facility defaultFacility = new Facility();

        // Verify all fields are null or zero
        assertNull(defaultFacility.getFacilityId());
        assertNull(defaultFacility.getName());
        assertNull(defaultFacility.getLocation());
        assertEquals(0, defaultFacility.getCapacity());
        assertNull(defaultFacility.getOrganizerId());
    }

    /**
     * Test the setters and getters of the Facility class.
     * Verifies that the setter methods correctly set the field values.
     */
    @Test
    public void testSettersAndGetters() {
        Facility updatedFacility = new Facility();

        updatedFacility.setFacilityId("facility2");
        updatedFacility.setName("Updated Facility");
        updatedFacility.setLocation("5678 Another St");
        updatedFacility.setCapacity(200);
        updatedFacility.setOrganizerId("organizer2");

        assertEquals("facility2", updatedFacility.getFacilityId());
        assertEquals("Updated Facility", updatedFacility.getName());
        assertEquals("5678 Another St", updatedFacility.getLocation());
        assertEquals(200, updatedFacility.getCapacity());
        assertEquals("organizer2", updatedFacility.getOrganizerId());
    }

    /**
     * Test the conversion of the Facility object to a Map representation.
     * This is used for storing facility data in a database or transferring it across systems.
     */
    @Test
    public void testToMapConversion() {
        // Convert the facility to a map and check if all fields are correctly mapped
        Map<String, Object> facilityMap = facility.toMap();

        assertNotNull(facilityMap);
        assertEquals("facility1", facilityMap.get("facilityId"));
        assertEquals("Facility One", facilityMap.get("name"));
        assertEquals("1234 Address St", facilityMap.get("location"));
        assertEquals(100, facilityMap.get("capacity"));
        assertEquals("organizer1", facilityMap.get("organizerId"));
    }
}
