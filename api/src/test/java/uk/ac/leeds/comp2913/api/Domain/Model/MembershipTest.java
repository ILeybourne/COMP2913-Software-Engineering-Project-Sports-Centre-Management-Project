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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {

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
    @MethodSource("membershipJsonVal")
    void membershipJson(Date accountCreatedAt, String membershipTypeName, Date createdAt, Date updatedAt, Date startDate, Date endDate, String expectedOutput) throws JsonProcessingException {
        // Create account object
        Account account = new Account();
        account.setCreatedAt(accountCreatedAt);

        // Create membership type object
        MembershipType membershipType = new MembershipType();
        membershipType.setName(membershipTypeName);

        // Create membership object
        Membership membership = new Membership();
        membership.setCreatedAt(createdAt);
        membership.setUpdatedAt(updatedAt);
        membership.setAccount(account);
        membership.setMembershipType(membershipType);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(membership);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> membershipJsonVal() {
        return Stream.of(
                Arguments.of(new Date(110, Calendar.JANUARY, 1), "TestMembership1", new Date(120, Calendar.JUNE, 2), new Date(121, Calendar.NOVEMBER, 3), new Date(122, Calendar.APRIL, 4), new Date(123, Calendar.SEPTEMBER, 5),
                        "{\"type\":\"membership\",\"amount\":null,\"createdAt\":1591052400000,\"endDate\":1693868400000,\"id\":0,\"name\":\"Membership TestMembership1\",\"repeatingPayment\":null,\"startDate\":1649026800000,\"updatedAt\":1635897600000}"),
                Arguments.of(new Date(110, Calendar.FEBRUARY, 6), "TestMembership2", new Date(120, Calendar.JULY, 7), new Date(121, Calendar.DECEMBER, 8), new Date(122, Calendar.MAY, 9), new Date(123, Calendar.OCTOBER, 10),
                        "{\"type\":\"membership\",\"amount\":null,\"createdAt\":1594076400000,\"endDate\":1696892400000,\"id\":0,\"name\":\"Membership TestMembership2\",\"repeatingPayment\":null,\"startDate\":1652050800000,\"updatedAt\":1638921600000}"),
                Arguments.of(new Date(110, Calendar.MARCH, 11), "TestMembership3", new Date(120, Calendar.AUGUST, 12), new Date(121, Calendar.JANUARY, 13), new Date(122, Calendar.JUNE, 14), new Date(123, Calendar.NOVEMBER, 15),
                        "{\"type\":\"membership\",\"amount\":null,\"createdAt\":1597186800000,\"endDate\":1700006400000,\"id\":0,\"name\":\"Membership TestMembership3\",\"repeatingPayment\":null,\"startDate\":1655161200000,\"updatedAt\":1610496000000}"),
                Arguments.of(new Date(110, Calendar.APRIL, 16), "TestMembership4", new Date(120, Calendar.SEPTEMBER, 17), new Date(121, Calendar.FEBRUARY, 18), new Date(122, Calendar.JULY, 19), new Date(123, Calendar.DECEMBER, 20),
                        "{\"type\":\"membership\",\"amount\":null,\"createdAt\":1600297200000,\"endDate\":1703030400000,\"id\":0,\"name\":\"Membership TestMembership4\",\"repeatingPayment\":null,\"startDate\":1658185200000,\"updatedAt\":1613606400000}"),
                Arguments.of(new Date(110, Calendar.MAY, 21), "TestMembership5", new Date(120, Calendar.OCTOBER, 22), new Date(121, Calendar.MARCH, 23), new Date(122, Calendar.AUGUST, 24), new Date(123, Calendar.JANUARY, 25),
                        "{\"type\":\"membership\",\"amount\":null,\"createdAt\":1603321200000,\"endDate\":1674604800000,\"id\":0,\"name\":\"Membership TestMembership5\",\"repeatingPayment\":null,\"startDate\":1661295600000,\"updatedAt\":1616457600000}")
        );
    }
}