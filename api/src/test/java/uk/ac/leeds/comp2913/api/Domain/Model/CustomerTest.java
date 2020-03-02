package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    Random random;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getId() {
        long testId = customer.getId();
        assertTrue(testId > 0);
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        customer.setCreatedAt(testDateSet);
        Date testDateGet = customer.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        customer.setCreatedAt(testDateSet);
        Date testDateGet = customer.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        customer.setUpdatedAt(testDateSet);
        Date testDateGet = customer.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        customer.setUpdatedAt(testDateSet);
        Date testDateGet = customer.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    @Disabled
    void userLogin() {
        // TODO Not implemented
    }
}