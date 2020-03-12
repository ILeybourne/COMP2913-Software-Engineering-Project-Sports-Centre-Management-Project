package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    Resource resource;
    Random random;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        String testNameSet = "test" + random.nextInt();
        resource.setName(testNameSet);
        String testNameGet = resource.getName();
        assertEquals(testNameSet, testNameGet);
    }

    @Test
    void setName() {
        String testNameSet = "test" + random.nextInt();
        resource.setName(testNameSet);
        String testNameGet = resource.getName();
        assertEquals(testNameSet, testNameGet);
    }

    @Test
    void getActivities() {
        Set<Activity> testActivitiesSet = Collections.singleton(new Activity());
        resource.setActivities(testActivitiesSet);
        Set<Activity> testActivitiesGet = resource.getActivities();
        assertTrue(testActivitiesGet.size() == 1);
    }

    @Test
    void setActivities() {
        Set<Activity> testActivitiesSet = Collections.singleton(new Activity());
        resource.setActivities(testActivitiesSet);
        Set<Activity> testActivitiesGet = resource.getActivities();
        assertTrue(testActivitiesGet.size() == 1);
    }
}
