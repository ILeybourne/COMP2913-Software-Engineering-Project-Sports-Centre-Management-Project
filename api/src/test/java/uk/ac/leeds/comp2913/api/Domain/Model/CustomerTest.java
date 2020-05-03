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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    ObjectMapper objectMapper;
    Customer customer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        customer = new Customer();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("customerJsonVal")
    void customerJson(Date dateOfBirth, String emailAddress, String expectedOutput) throws JsonProcessingException {
        // Create customer object
        Customer customer = new Customer();
        customer.setDateOfBirth(dateOfBirth);
        customer.setReceipts(new ArrayList<>());
        customer.setAccount(new ArrayList<>());
        customer.setEmailAddress(emailAddress);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(customer);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> customerJsonVal() {
        return Stream.of(
                Arguments.of(new Date(120, Calendar.JANUARY, 1), "a@a.com",
                        "{\"dateOfBirth\":1577836800000,\"emailAddress\":\"a@a.com\",\"id\":null,\"stripeId\":null}"),
                Arguments.of(new Date(121, Calendar.MARCH, 2), "b@b.com",
                        "{\"dateOfBirth\":1614643200000,\"emailAddress\":\"b@b.com\",\"id\":null,\"stripeId\":null}"),
                Arguments.of(new Date(122, Calendar.MAY, 3), "c@c.com",
                        "{\"dateOfBirth\":1651532400000,\"emailAddress\":\"c@c.com\",\"id\":null,\"stripeId\":null}"),
                Arguments.of(new Date(123, Calendar.JULY, 4), "d@d.com",
                        "{\"dateOfBirth\":1688425200000,\"emailAddress\":\"d@d.com\",\"id\":null,\"stripeId\":null}"),
                Arguments.of(new Date(124, Calendar.SEPTEMBER, 5), "e@e.com",
                        "{\"dateOfBirth\":1725490800000,\"emailAddress\":\"e@e.com\",\"id\":null,\"stripeId\":null}")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void addReceipt(int id) {
        // Create receipt
        Receipt receipt = new Receipt();
        receipt.setId(id);

        // Add receipt to customer
        customer.addReceipt(receipt);

        // Check receipt is in customer list
        assertTrue(customer.getReceipts().contains(receipt));

        // Check customer is attached to receipt
        assertEquals(customer, receipt.getCustomer());
    }
}
