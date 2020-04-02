package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    Activity activity;
    Random random;
    Resource resource;

    @BeforeEach
    void setUp() {
        activity = new Activity();
        random = new Random();
        resource = new Resource();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getId() {
        long testIdSet = random.nextLong();
        activity.setId(testIdSet);
        long testIdGet = activity.getId();
        assertEquals(testIdSet, testIdGet);
    }

    @Test
    void setId() {
        long testIdSet = random.nextLong();
        activity.setId(testIdSet);
        long testIdGet = activity.getId();
        assertEquals(testIdSet, testIdGet);
    }

    @Test
    void getStart_time() {
        Date testDateSet = new Date(random.nextLong());
        activity.setStartTime(testDateSet);
        Date testDateGet = activity.getStartTime();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setStart_time() {
        Date testDateSet = new Date(random.nextLong());
        activity.setStartTime(testDateSet);
        Date testDateGet = activity.getStartTime();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getEnd_time() {
        Date testDateSet = new Date(random.nextLong());
        activity.setStartTime(testDateSet);
        Date testDateGet = activity.getStartTime();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setEnd_time() {
        Date testDateSet = new Date(random.nextLong());
        activity.setStartTime(testDateSet);
        Date testDateGet = activity.getStartTime();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getResource() {
        activity.setResource(resource);
        Resource testGetResource = activity.getResource();
        assertEquals(resource, testGetResource);
    }

    @Test
    void setResource() {
        activity.setResource(resource);
        Resource testGetResource = activity.getResource();
        assertEquals(resource, testGetResource);
    }
}