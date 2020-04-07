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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

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
    @MethodSource("accountJsonVal")
    void accountJson(String centreName, long membershipId, String customerEmailAddress, Date createdAt, Date updatedAt, String expectedOutput) throws JsonProcessingException {
        // Create centre
        Centre centre = new Centre();
        centre.setName(centreName);

        // Create membership
        Membership membership = new Membership();
        membership.id = membershipId;

        // Create customer
        Customer customer = new Customer();
        customer.setEmailAddress(customerEmailAddress);

        // Create account object
        Account account = new Account();
        account.setCreatedAt(createdAt);
        account.setUpdatedAt(updatedAt);
        account.setCentre(centre);
        account.setMemberships(membership);
        account.setCustomer(customer);
        account.setBookings(new ArrayList<>());

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(account);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> accountJsonVal() {
        return Stream.of(
                Arguments.of("Jeff", 123, "jeff@jeff.net", new Date(130, Calendar.MAY, 4), new Date(130, Calendar.OCTOBER, 25),
                        "{\"bookings\":[],\"createdAt\":1904079600000,\"customer\":{\"account\":null,\"dateOfBirth\":null,\"emailAddress\":\"jeff@jeff.net\",\"receipts\":[]},\"id\":0,\"updatedAt\":1919113200000}"),
                Arguments.of("Todd", 456, "todd@todd.net", new Date(130, Calendar.OCTOBER, 21), new Date(131, Calendar.DECEMBER, 2),
                        "{\"bookings\":[],\"createdAt\":1918767600000,\"customer\":{\"account\":null,\"dateOfBirth\":null,\"emailAddress\":\"todd@todd.net\",\"receipts\":[]},\"id\":0,\"updatedAt\":1953936000000}"),
                Arguments.of("Eric", -1, "eric@eric.net", new Date(90, Calendar.JANUARY, 1), new Date(91, Calendar.NOVEMBER, 22),
                        "{\"bookings\":[],\"createdAt\":631152000000,\"customer\":{\"account\":null,\"dateOfBirth\":null,\"emailAddress\":\"eric@eric.net\",\"receipts\":[]},\"id\":0,\"updatedAt\":690768000000}"),
                Arguments.of("Mike", 286, "mike@mike.net", new Date(10, Calendar.MAY, 7), new Date(11, Calendar.AUGUST, 30),
                        "{\"bookings\":[],\"createdAt\":-1882569600000,\"customer\":{\"account\":null,\"dateOfBirth\":null,\"emailAddress\":\"mike@mike.net\",\"receipts\":[]},\"id\":0,\"updatedAt\":-1841097600000}"),
                Arguments.of("Brad", 321, "brad@brad.net", new Date(1000, Calendar.FEBRUARY, 19), new Date(1001, Calendar.JUNE, 28),
                        "{\"bookings\":[],\"createdAt\":29352240000000,\"customer\":{\"account\":null,\"dateOfBirth\":null,\"emailAddress\":\"brad@brad.net\",\"receipts\":[]},\"id\":0,\"updatedAt\":29394918000000}")
        );
    }
}