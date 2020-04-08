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
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTypeTest {

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
    @MethodSource("membershipTypeJsonVal")
    void membershipTypeJson(long id, Date createdAt, Date updatedAt, String name, int duration, BigDecimal cost, String expectedOutput) throws JsonProcessingException {
        // Create membershipType object
        MembershipType membershipType = new MembershipType();
        membershipType.setId(id);
        membershipType.setCreatedAt(createdAt);
        membershipType.setUpdatedAt(updatedAt);
        membershipType.setName(name);
        membershipType.setDuration(duration);
        membershipType.setCost(cost);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(membershipType);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> membershipTypeJsonVal() {
        return Stream.of(
                Arguments.of(111, new Date(120, Calendar.JANUARY, 1), new Date(125, Calendar.JUNE, 6), "TestMembershipType1", 100, BigDecimal.valueOf(200),
                        "{\"cost\":200,\"createdAt\":1577836800000,\"duration\":100,\"id\":111,\"name\":\"TestMembershipType1\",\"updatedAt\":1749164400000}"),
                Arguments.of(222, new Date(121, Calendar.FEBRUARY, 2), new Date(126, Calendar.JULY, 7), "TestMembershipType2", 300, BigDecimal.valueOf(400),
                        "{\"cost\":400,\"createdAt\":1612224000000,\"duration\":300,\"id\":222,\"name\":\"TestMembershipType2\",\"updatedAt\":1783378800000}"),
                Arguments.of(333, new Date(122, Calendar.MARCH, 3), new Date(127, Calendar.AUGUST, 8), "TestMembershipType3", 500, BigDecimal.valueOf(600),
                        "{\"cost\":600,\"createdAt\":1646265600000,\"duration\":500,\"id\":333,\"name\":\"TestMembershipType3\",\"updatedAt\":1817679600000}"),
                Arguments.of(444, new Date(123, Calendar.APRIL, 4), new Date(128, Calendar.SEPTEMBER, 9), "TestMembershipType4", 700, BigDecimal.valueOf(700),
                        "{\"cost\":700,\"createdAt\":1680562800000,\"duration\":700,\"id\":444,\"name\":\"TestMembershipType4\",\"updatedAt\":1852066800000}"),
                Arguments.of(555, new Date(124, Calendar.MAY, 5), new Date(129, Calendar.OCTOBER, 10), "TestMembershipType5", 900, BigDecimal.valueOf(1000),
                        "{\"cost\":1000,\"createdAt\":1714863600000,\"duration\":900,\"id\":555,\"name\":\"TestMembershipType5\",\"updatedAt\":1886281200000}")
        );
    }
}