package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActivityTest {

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
    @MethodSource("activityJsonVal")
    void activityJson(String activityTypeName, long resourceId, long regularSessionId, long id, Date startTime, Date endTime, String name, int capacity, BigDecimal cost, boolean social, String expectedOutput) throws JsonProcessingException {
        // Create activity type
        ActivityType activityType = new ActivityType();
        activityType.setName(activityTypeName);

        // Create resource
        Resource resource = new Resource();
        resource.setId(resourceId);

        // Create regular session
        RegularSession regularSession = new RegularSession();
        regularSession.setId(regularSessionId);

        // Create activity object
        Activity activity = new Activity();
        activity.setActivityType(activityType);
        activity.setId(id);
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setName(name);
        activity.setCurrentCapacity(capacity);
        activity.setBookings(new HashSet<>());
        activity.setResource(resource);
        activity.setCost(cost);
        activity.setSocial(social);
        activity.setRegularSession(regularSession);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(activity);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> activityJsonVal() {
        return Stream.of(
                Arguments.of("TestType1", 1, 6, 11, new Date(120, Calendar.JANUARY, 1), new Date(121, Calendar.JUNE, 6), "TestActivity1", 100, BigDecimal.valueOf(72), false,
                        "{\"cost\":72,\"currentCapacity\":100,\"endTime\":1622934000000,\"id\":11,\"name\":\"TestActivity1\",\"regularSession\":{\"id\":6,\"interval\":null},\"social\":false,\"startTime\":1577836800000}"),
                Arguments.of("TestType2", 2, 7, 12, new Date(120, Calendar.FEBRUARY, 2), new Date(121, Calendar.JULY, 7), "TestActivity2", 123, BigDecimal.valueOf(54), true,
                        "{\"cost\":54,\"currentCapacity\":123,\"endTime\":1625612400000,\"id\":12,\"name\":\"TestActivity2\",\"regularSession\":{\"id\":7,\"interval\":null},\"social\":true,\"startTime\":1580601600000}"),
                Arguments.of("TestType3", 3, 8, 13, new Date(120, Calendar.MARCH, 3), new Date(121, Calendar.AUGUST, 8), "TestActivity3", 456, BigDecimal.valueOf(34), false,
                        "{\"cost\":34,\"currentCapacity\":456,\"endTime\":1628377200000,\"id\":13,\"name\":\"TestActivity3\",\"regularSession\":{\"id\":8,\"interval\":null},\"social\":false,\"startTime\":1583193600000}"),
                Arguments.of("TestType4", 4, 9, 14, new Date(120, Calendar.APRIL, 4), new Date(121, Calendar.SEPTEMBER, 9), "TestActivity4", 789, BigDecimal.valueOf(101), true,
                        "{\"cost\":101,\"currentCapacity\":789,\"endTime\":1631142000000,\"id\":14,\"name\":\"TestActivity4\",\"regularSession\":{\"id\":9,\"interval\":null},\"social\":true,\"startTime\":1585954800000}"),
                Arguments.of("TestType5", 5, 10, 15, new Date(120, Calendar.MAY, 5), new Date(121, Calendar.OCTOBER, 10), "TestActivity5", 2000, BigDecimal.valueOf(21), false,
                        "{\"cost\":21,\"currentCapacity\":2000,\"endTime\":1633820400000,\"id\":15,\"name\":\"TestActivity5\",\"regularSession\":{\"id\":10,\"interval\":null},\"social\":false,\"startTime\":1588633200000}")
        );
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