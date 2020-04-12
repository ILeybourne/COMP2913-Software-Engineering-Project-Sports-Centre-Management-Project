package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceTest {

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("resourceJsonVal")
    void resourceJson(String name, long id, String expectedOutput) throws JsonProcessingException {
        // Create resource object
        Resource resource = new Resource();
        resource.setName(name);
        resource.setActivities(new HashSet<>());
        resource.setId(id);
        resource.setActivityTypes(new HashSet<>());

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(resource);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> resourceJsonVal() {
        return Stream.of(
                Arguments.of("TestResource1", 1,
                        "{\"createdAt\":null,\"id\":1,\"name\":\"TestResource1\",\"updatedAt\":null}"),
                Arguments.of("TestResource2", 2,
                        "{\"createdAt\":null,\"id\":2,\"name\":\"TestResource2\",\"updatedAt\":null}"),
                Arguments.of("TestResource3", 3,
                        "{\"createdAt\":null,\"id\":3,\"name\":\"TestResource3\",\"updatedAt\":null}"),
                Arguments.of("TestResource4", 4,
                        "{\"createdAt\":null,\"id\":4,\"name\":\"TestResource4\",\"updatedAt\":null}"),
                Arguments.of("TestResource5", 5,
                        "{\"createdAt\":null,\"id\":5,\"name\":\"TestResource5\",\"updatedAt\":null}")
        );
    }

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
