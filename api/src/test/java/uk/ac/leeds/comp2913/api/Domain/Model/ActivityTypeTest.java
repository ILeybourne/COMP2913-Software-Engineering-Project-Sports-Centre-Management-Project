package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTypeTest {

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
    @MethodSource("activityTypeJsonVal")
    void activityTypeJson(long resourceId, long id, Date createdAt, Date updatedAt, String name, BigDecimal cost, int totalCapacity, String expectedOutput) throws JsonProcessingException {
        // Create resource object
        Resource resource = new Resource();
        resource.setId(resourceId);

        // Create activity type object
        ActivityType activityType = new ActivityType();
        activityType.setId(id);
        activityType.setCreatedAt(createdAt);
        activityType.setUpdatedAt(updatedAt);
        activityType.setName(name);
        activityType.setCost(cost);
        activityType.setTotalCapacity(totalCapacity);
        activityType.setResource(resource);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(activityType);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> activityTypeJsonVal() {
        return Stream.of(
                Arguments.of(1, 6, new Date(120, Calendar.JANUARY, 1), new Date(121, Calendar.JUNE, 6), "TestType1", BigDecimal.valueOf(100), 200,
                        "{\"cost\":100,\"createdAt\":1577836800000,\"id\":6,\"name\":\"TestType1\",\"resource\":{\"id\":1,\"name\":null},\"totalCapacity\":200,\"updatedAt\":1622934000000}"),
                Arguments.of(2, 7, new Date(120, Calendar.JANUARY, 2), new Date(121, Calendar.JUNE, 7), "TestType2", BigDecimal.valueOf(200), 400,
                        "{\"cost\":200,\"createdAt\":1577923200000,\"id\":7,\"name\":\"TestType2\",\"resource\":{\"id\":2,\"name\":null},\"totalCapacity\":400,\"updatedAt\":1623020400000}"),
                Arguments.of(3, 8, new Date(120, Calendar.JANUARY, 3), new Date(121, Calendar.JUNE, 8), "TestType3", BigDecimal.valueOf(300), 600,
                        "{\"cost\":300,\"createdAt\":1578009600000,\"id\":8,\"name\":\"TestType3\",\"resource\":{\"id\":3,\"name\":null},\"totalCapacity\":600,\"updatedAt\":1623106800000}"),
                Arguments.of(4, 9, new Date(120, Calendar.JANUARY, 4), new Date(121, Calendar.JUNE, 9), "TestType4", BigDecimal.valueOf(400), 800,
                        "{\"cost\":400,\"createdAt\":1578096000000,\"id\":9,\"name\":\"TestType4\",\"resource\":{\"id\":4,\"name\":null},\"totalCapacity\":800,\"updatedAt\":1623193200000}"),
                Arguments.of(5, 10, new Date(120, Calendar.JANUARY, 5), new Date(121, Calendar.JUNE, 10), "TestType5", BigDecimal.valueOf(500), 1000,
                        "{\"cost\":500,\"createdAt\":1578182400000,\"id\":10,\"name\":\"TestType5\",\"resource\":{\"id\":5,\"name\":null},\"totalCapacity\":1000,\"updatedAt\":1623279600000}")
        );
    }
}
