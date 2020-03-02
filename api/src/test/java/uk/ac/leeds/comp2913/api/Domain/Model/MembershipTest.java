package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {

    Membership membership;
    Random random;

    @BeforeEach
    void setUp() {
        membership = new Membership();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getId() {
        long testId = membership.getId();
        assertTrue(testId > 0);
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setCreatedAt(testDateSet);
        Date testDateGet = membership.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setCreatedAt(testDateSet);
        Date testDateGet = membership.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setUpdatedAt(testDateSet);
        Date testDateGet = membership.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setUpdatedAt(testDateSet);
        Date testDateGet = membership.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    @Disabled
    void makePayment() {
    }

    @Test
    @Disabled
    void emailReceipt() {
    }

    @Test
    @Disabled
    void printReceipt() {
    }

    @Test
    @Disabled
    void storeReceipt() {
    }
}