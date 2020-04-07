package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5})
    void createBookingFromRegularSession(long id) {
        // Create activity
        Activity activity = new Activity();
        activity.setId(id);

        // Create copy of booking
        Booking expectedOutput = new Booking();
        Booking actualOutput = Booking.createBookingFromRegularSession(activity, expectedOutput);

        // Perform assertions
        assertNotEquals(expectedOutput.getActivity(), actualOutput.getActivity());
        assertEquals(expectedOutput.getRegularSession(), actualOutput.getRegularSession());
        assertEquals(expectedOutput.getParticipants(), actualOutput.getParticipants());
        assertEquals(expectedOutput.getAccount(), actualOutput.getAccount());
        assertEquals(expectedOutput.amount, actualOutput.amount);
    }

    @ParameterizedTest
    @MethodSource("calculateRegularSessionAmountVal")
    void calculateRegularSessionAmount(BigDecimal inputValue, BigDecimal expectedValue) {
        // Calculate session amount
        BigDecimal actualValue = booking.calculateRegularSessionAmount(inputValue);

        // Compare value
        assertThat(expectedValue, Matchers.comparesEqualTo(actualValue));
    }

    static Stream<Arguments> calculateRegularSessionAmountVal() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(1), BigDecimal.valueOf(0.7)),
                Arguments.of(BigDecimal.valueOf(5), BigDecimal.valueOf(3.5)),
                Arguments.of(BigDecimal.valueOf(1000000), BigDecimal.valueOf(700000)),
                Arguments.of(BigDecimal.valueOf(-10), BigDecimal.valueOf(-7)),
                Arguments.of(BigDecimal.valueOf(0.001), BigDecimal.valueOf(0.0007))
        );
    }
}