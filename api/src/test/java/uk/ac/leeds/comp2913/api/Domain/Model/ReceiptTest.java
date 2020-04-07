package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import javafx.scene.control.RadioMenuItem;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiptTest {

    Receipt receipt;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("generateFilenameVal")
    void generateFilename(int id, Date date, String expectedOutput) {
        // Set example values
        receipt.setId(id);
        receipt.setCreatedAt(date);

        // Generate file name using method
        String actualOutput = receipt.generateFilename();

        // Assert values are equal
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> generateFilenameVal() {
        return Stream.of(
                Arguments.of(123, new Date(120, Calendar.MAY, 21), "123-21-05-2020"),
                Arguments.of(456, new Date(98, Calendar.OCTOBER, 25), "456-25-10-1998"),
                Arguments.of(978634987, new Date(130, Calendar.APRIL, 12), "978634987-12-04-2030"),
                Arguments.of(0, new Date(1, Calendar.JANUARY, 11), "0-11-01-1901"),
                Arguments.of(-1, new Date(12, Calendar.DECEMBER, 7), "-1-7-12-1912")
        );
    }
}
