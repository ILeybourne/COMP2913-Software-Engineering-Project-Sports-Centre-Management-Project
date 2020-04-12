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

import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegularSessionTest {

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
    @MethodSource("regularSessionJsonVal")
    void regularSessionJson(long id, int interval, String expectedOutput) throws JsonProcessingException {
        // Create regular session object
        RegularSession regularSession = new RegularSession();
        regularSession.setId(id);
        regularSession.setActivities(new HashSet<>());
        regularSession.setInterval(interval);
        regularSession.setBookings(new HashSet<>());

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(regularSession);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> regularSessionJsonVal() {
        return Stream.of(
                Arguments.of(111, 5,
                        "{\"activities\":[],\"id\":111,\"interval\":5}"),
                Arguments.of(222, 4,
                        "{\"activities\":[],\"id\":222,\"interval\":4}"),
                Arguments.of(333, 3,
                        "{\"activities\":[],\"id\":333,\"interval\":3}"),
                Arguments.of(444, 2,
                        "{\"activities\":[],\"id\":444,\"interval\":2}"),
                Arguments.of(555, 1,
                        "{\"activities\":[],\"id\":555,\"interval\":1}")
        );
    }
}