package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceTest {

//    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void example() throws JsonProcessingException {
//        String serialised = objectMapper.writeValueAsString(resource);
//        assertFalse(serialised.contains("activityType"));
//    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void addActivityType(int id) {
        // Create resource object
        Resource resource = new Resource();
        resource.setActivityTypes(new HashSet<>());

        // Set activity type
        ActivityType testActivityType = new ActivityType();
        testActivityType.setId(id);
        resource.addActivityType(testActivityType);

        // Test activity type is stored
        Set<ActivityType> activityTypes = resource.getActivityTypes();
        assertTrue(activityTypes.contains(testActivityType));

        // Test activity type resource is set
        assertEquals(resource, testActivityType.getResource());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void removeActivityType(int id) {
        // Create resource object
        Resource resource = new Resource();
        resource.setActivityTypes(new HashSet<>());

        // Set activity type
        ActivityType testActivityType = new ActivityType();
        testActivityType.setId(id);
        resource.addActivityType(testActivityType);

        // Removed resource type
        resource.removeActivityType(testActivityType);

        // Test activity type is stored
        Set<ActivityType> activityTypes = resource.getActivityTypes();
        assertFalse(activityTypes.contains(testActivityType));

        // Test activity type resource is set
        assertNull(testActivityType.getResource());
    }
}
