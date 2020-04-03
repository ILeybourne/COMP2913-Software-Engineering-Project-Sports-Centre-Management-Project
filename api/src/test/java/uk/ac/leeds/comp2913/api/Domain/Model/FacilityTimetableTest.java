package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FacilityTimetableTest {

    FacilityTimetable facilityTimetable;
    Random random;

    @BeforeEach
    void setUp() {
        facilityTimetable = new FacilityTimetable();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        facilityTimetable.setCreatedAt(testDateSet);
        Date testDateGet = facilityTimetable.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        facilityTimetable.setCreatedAt(testDateSet);
        Date testDateGet = facilityTimetable.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        facilityTimetable.setUpdatedAt(testDateSet);
        Date testDateGet = facilityTimetable.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        facilityTimetable.setUpdatedAt(testDateSet);
        Date testDateGet = facilityTimetable.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getName() {
        String testNameSet = Integer.toString(random.nextInt());
        facilityTimetable.setName(testNameSet);
        String testNameGet = facilityTimetable.getName();
        assertEquals(testNameSet, testNameGet);
    }

    @Test
    void setName() {
        String testNameSet = Integer.toString(random.nextInt());
        facilityTimetable.setName(testNameSet);
        String testNameGet = facilityTimetable.getName();
        assertEquals(testNameSet, testNameGet);
    }

    @Test
    void getResources() {
        List<Resource> testResourceSet = new ArrayList<Resource>();
        Resource testResource = new Resource();
        testResourceSet.add(testResource);
        facilityTimetable.setResources(testResourceSet);
        List<Resource> testResourceGet = facilityTimetable.getResources();
        assertEquals(testResourceGet, testResourceSet);
    }

    @Test
    void setResources() {
        List<Resource> testResourceSet = new ArrayList<Resource>();
        Resource testResource = new Resource();
        testResourceSet.add(testResource);
        facilityTimetable.setResources(testResourceSet);
        List<Resource> testResourceGet = facilityTimetable.getResources();
        assertEquals(testResourceGet, testResourceSet);
    }

    @Test
    void getCentre() {
        Centre testCentreSet = new Centre();
        facilityTimetable.setCentre(testCentreSet);
        Centre testCentreGet = facilityTimetable.getCentre();
        assertEquals(testCentreGet, testCentreSet);
    }

    @Test
    void setCentre() {
        Centre testCentreSet = new Centre();
        facilityTimetable.setCentre(testCentreSet);
        Centre testCentreGet = facilityTimetable.getCentre();
        assertEquals(testCentreGet, testCentreSet);
    }
}