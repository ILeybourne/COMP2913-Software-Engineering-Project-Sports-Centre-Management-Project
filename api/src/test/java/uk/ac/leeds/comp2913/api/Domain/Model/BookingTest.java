package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    Booking booking;
    Random random;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        booking.setCreatedAt(testDateSet);
        Date testDateGet = booking.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        booking.setCreatedAt(testDateSet);
        Date testDateGet = booking.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        booking.setUpdatedAt(testDateSet);
        Date testDateGet = booking.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        booking.setUpdatedAt(testDateSet);
        Date testDateGet = booking.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }
}