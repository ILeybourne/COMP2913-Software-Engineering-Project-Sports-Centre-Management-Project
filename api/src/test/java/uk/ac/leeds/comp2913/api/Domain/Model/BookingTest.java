package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    ObjectMapper objectMapper;
    Booking booking;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        booking = new Booking();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("bookingJsonVal")
    void bookingJson(Date accountCreatedAt, long activityId, long regularSessionId, Date createdAt, Date updatedAt, int participants, String expectedOutput) throws JsonProcessingException {
        // Create account
        Account account = new Account();
        account.setCreatedAt(accountCreatedAt);

        // Create activity
        Activity activity = new Activity();
        activity.setId(activityId);

        // Create regular session
        RegularSession regularSession = new RegularSession();
        regularSession.setId(regularSessionId);

        // Create booking object
        Booking booking = new Booking();
        booking.setCreatedAt(createdAt);
        booking.setUpdatedAt(updatedAt);
        booking.setAccount(account);
        booking.setActivity(activity);
        booking.setParticipants(participants);
        booking.setRegularSession(regularSession);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(booking);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> bookingJsonVal() {
        return Stream.of(
                Arguments.of(new Date(100, Calendar.DECEMBER, 11), 1, 6, new Date(120, Calendar.JANUARY, 1), new Date(120, Calendar.JUNE, 6), 100,
                        "{\"type\":\"booking\",\"amount\":null,\"id\":0,\"name\":\"Booking on Wed Jan 01 00:00:00 GMT 2020\",\"participants\":100}"),
                Arguments.of(new Date(100, Calendar.NOVEMBER, 12), 2, 7, new Date(120, Calendar.FEBRUARY, 2), new Date(120, Calendar.JULY, 7), 200,
                        "{\"type\":\"booking\",\"amount\":null,\"id\":0,\"name\":\"Booking on Sun Feb 02 00:00:00 GMT 2020\",\"participants\":200}"),
                Arguments.of(new Date(100, Calendar.OCTOBER, 13), 3, 8, new Date(120, Calendar.MARCH, 3), new Date(120, Calendar.AUGUST, 8), 300,
                        "{\"type\":\"booking\",\"amount\":null,\"id\":0,\"name\":\"Booking on Tue Mar 03 00:00:00 GMT 2020\",\"participants\":300}"),
                Arguments.of(new Date(100, Calendar.SEPTEMBER, 14), 4, 9, new Date(120, Calendar.APRIL, 4), new Date(120, Calendar.SEPTEMBER, 9), 400,
                        "{\"type\":\"booking\",\"amount\":null,\"id\":0,\"name\":\"Booking on Sat Apr 04 00:00:00 BST 2020\",\"participants\":400}"),
                Arguments.of(new Date(100, Calendar.AUGUST, 15), 5, 10, new Date(120, Calendar.MAY, 5), new Date(120, Calendar.OCTOBER, 10), 500,
                        "{\"type\":\"booking\",\"amount\":null,\"id\":0,\"name\":\"Booking on Tue May 05 00:00:00 BST 2020\",\"participants\":500}")
        );
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

  // @ParameterizedTest
  // @MethodSource("calculateRegularSessionAmountVal")
  // void calculateRegularSessionAmount(BigDecimal inputValue, BigDecimal expectedValue) {
  //     // Calculate session amount
  //     BigDecimal actualValue = booking.calculateRegularSessionAmount(inputValue);

  //     // Compare value
  //     assertThat(expectedValue, Matchers.comparesEqualTo(actualValue));
  // }

  // static Stream<Arguments> calculateRegularSessionAmountVal() {
  //     return Stream.of(
  //             Arguments.of(BigDecimal.valueOf(1), BigDecimal.valueOf(0.7)),
  //             Arguments.of(BigDecimal.valueOf(5), BigDecimal.valueOf(3.5)),
  //             Arguments.of(BigDecimal.valueOf(1000000), BigDecimal.valueOf(700000)),
  //             Arguments.of(BigDecimal.valueOf(-10), BigDecimal.valueOf(-7)),
  //             Arguments.of(BigDecimal.valueOf(0.001), BigDecimal.valueOf(0.0007))
  //     );
  // }
}