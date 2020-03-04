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

    @BeforeEach
    void setUp() {
        activity = new Activity();
        random = new Random();
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
    @Disabled
    void getResource() {
        // TODO Not implemented
    }
}