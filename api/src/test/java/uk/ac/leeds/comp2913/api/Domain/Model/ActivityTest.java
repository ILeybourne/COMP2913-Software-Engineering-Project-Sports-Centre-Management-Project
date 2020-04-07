package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActivityTest {

    Random random;

    @BeforeEach
    void setUp() {
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("createFromLastScheduledVal")
    void createFromLastScheduled(int interval, long id, String name, BigDecimal cost, Date startTime, Date endTime) {
        // Create regular session with interval
        RegularSession regularSession = new RegularSession();
        regularSession.setInterval(interval);

        // Create activity type
        ActivityType activityType = new ActivityType();
        activityType.setName(name);
        activityType.setResource(new Resource());
        activityType.setCost(cost);

        // Create copy of activity
        Activity expectedOutput = new Activity();
        expectedOutput.setId(id);
        expectedOutput.setActivityType(activityType);
        expectedOutput.setRegularSession(regularSession);
        expectedOutput.setStartTime(startTime);
        expectedOutput.setEndTime(endTime);
        Activity actualOutput = Activity.createFromLastScheduled(expectedOutput);

        // Assert ids are different
        assertEquals(expectedOutput.getId(), id);
        assertNull(actualOutput.getId());

        // Perform assertions
        assertEquals(expectedOutput.getRegularSession(), actualOutput.getRegularSession());
        assertEquals(expectedOutput.getActivityType(), actualOutput.getActivityType());
        assertNotEquals(expectedOutput.getStartTime(), actualOutput.getStartTime());
        assertNotEquals(expectedOutput.getEndTime(), actualOutput.getEndTime());
        assertEquals(expectedOutput.getCost(), actualOutput.getCost());
        assertEquals(expectedOutput.getResource(), actualOutput.getResource());
    }

    static Stream<Arguments> createFromLastScheduledVal() {
        return Stream.of(
                Arguments.of(3, 1, "TestName1", BigDecimal.valueOf(1.99), new Date(72, Calendar.MARCH, 24), new Date(120, Calendar.APRIL, 7)),
                Arguments.of(5, 2, "TestName2", BigDecimal.valueOf(3.56), new Date(72, Calendar.APRIL, 14), new Date(120, Calendar.APRIL, 8)),
                Arguments.of(2, 3, "TestName3", BigDecimal.valueOf(10.41), new Date(98, Calendar.OCTOBER, 20), new Date(120, Calendar.APRIL, 9)),
                Arguments.of(4, 4, "TestName4", BigDecimal.valueOf(0.01), new Date(100, Calendar.JULY, 20), new Date(120, Calendar.APRIL, 10)),
                Arguments.of(1, 5, "TestName5", BigDecimal.valueOf(5), new Date(106, Calendar.JANUARY, 9), new Date(120, Calendar.APRIL, 11))
        );
    }

    @ParameterizedTest
    @MethodSource("addIntervalToDateVal")
    void addIntervalToDate(Date startDate, int interval, Date expectedDate) {
        // Calculate date using method
        Date actualDate = Activity.addIntervalToDate(startDate, interval);

        // Compare values
        assertEquals(expectedDate, actualDate);
    }

    static Stream<Arguments> addIntervalToDateVal() {
        return Stream.of(
                Arguments.of(new Date(120, Calendar.APRIL, 2), 2, new Date(120, Calendar.APRIL, 4)),
                Arguments.of(new Date(120, Calendar.APRIL, 4), -2, new Date(120, Calendar.APRIL, 2)),
                Arguments.of(new Date(120, Calendar.APRIL, 2), 30, new Date(120, Calendar.MAY, 2)),
                Arguments.of(new Date(120, Calendar.APRIL, 2), -30, new Date(120, Calendar.MARCH, 3)),
                Arguments.of(new Date(120, Calendar.APRIL, 2), 365, new Date(121, Calendar.APRIL, 2))
        );
    }
}