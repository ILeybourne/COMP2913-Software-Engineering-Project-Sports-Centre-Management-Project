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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FacilityTimetableTest {

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
    @MethodSource("facilityTimetableJsonVal")
    void facilityTimetableJson(String centreName, String name, Date createdAt, Date updatedAt, String expectedOutput) throws JsonProcessingException {
        // Create centre object
        Centre centre = new Centre();
        centre.setName(centreName);

        // Create facilityTimetable object
        FacilityTimetable facilityTimetable = new FacilityTimetable();
        facilityTimetable.setName(name);
        facilityTimetable.setResources(new ArrayList<>());
        facilityTimetable.setCentre(centre);
        facilityTimetable.setCreatedAt(createdAt);
        facilityTimetable.setUpdatedAt(updatedAt);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(facilityTimetable);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> facilityTimetableJsonVal() {
        return Stream.of(
                Arguments.of("TestCentre1", "TestFacilityTimetable1", new Date(120, Calendar.JANUARY, 1), new Date(121, Calendar.JUNE, 6),
                        "{\"centre\":{\"accounts\":null,\"createdAt\":null,\"id\":null,\"name\":\"TestCentre1\",\"resources\":null,\"updatedAt\":null},\"createdAt\":1577836800000,\"id\":null,\"name\":\"TestFacilityTimetable1\",\"resources\":[],\"updatedAt\":1622934000000}"),
                Arguments.of("TestCentre2", "TestFacilityTimetable2", new Date(120, Calendar.FEBRUARY, 2), new Date(121, Calendar.JULY, 7),
                        "{\"centre\":{\"accounts\":null,\"createdAt\":null,\"id\":null,\"name\":\"TestCentre2\",\"resources\":null,\"updatedAt\":null},\"createdAt\":1580601600000,\"id\":null,\"name\":\"TestFacilityTimetable2\",\"resources\":[],\"updatedAt\":1625612400000}"),
                Arguments.of("TestCentre3", "TestFacilityTimetable3", new Date(120, Calendar.MARCH, 3), new Date(121, Calendar.AUGUST, 8),
                        "{\"centre\":{\"accounts\":null,\"createdAt\":null,\"id\":null,\"name\":\"TestCentre3\",\"resources\":null,\"updatedAt\":null},\"createdAt\":1583193600000,\"id\":null,\"name\":\"TestFacilityTimetable3\",\"resources\":[],\"updatedAt\":1628377200000}"),
                Arguments.of("TestCentre4", "TestFacilityTimetable4", new Date(120, Calendar.APRIL, 4), new Date(121, Calendar.SEPTEMBER, 9),
                        "{\"centre\":{\"accounts\":null,\"createdAt\":null,\"id\":null,\"name\":\"TestCentre4\",\"resources\":null,\"updatedAt\":null},\"createdAt\":1585954800000,\"id\":null,\"name\":\"TestFacilityTimetable4\",\"resources\":[],\"updatedAt\":1631142000000}"),
                Arguments.of("TestCentre5", "TestFacilityTimetable5", new Date(120, Calendar.MAY, 5), new Date(121, Calendar.OCTOBER, 10),
                        "{\"centre\":{\"accounts\":null,\"createdAt\":null,\"id\":null,\"name\":\"TestCentre5\",\"resources\":null,\"updatedAt\":null},\"createdAt\":1588633200000,\"id\":null,\"name\":\"TestFacilityTimetable5\",\"resources\":[],\"updatedAt\":1633820400000}")
        );
    }
}