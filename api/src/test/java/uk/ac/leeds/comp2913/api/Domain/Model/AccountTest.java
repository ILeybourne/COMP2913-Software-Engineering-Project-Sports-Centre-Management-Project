package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    Account account;
    Random random;

    @BeforeEach
    void setUp() {
        account = new Account();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        account.setCreatedAt(testDateSet);
        Date testDateGet = account.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        account.setCreatedAt(testDateSet);
        Date testDateGet = account.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        account.setUpdatedAt(testDateSet);
        Date testDateGet = account.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        account.setUpdatedAt(testDateSet);
        Date testDateGet = account.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    @Disabled
    void buyMembership() {
        // TODO Not implemented
    }

    @Test
    @Disabled
    void cancelMembership() {
        // TODO Not implemented
    }
}