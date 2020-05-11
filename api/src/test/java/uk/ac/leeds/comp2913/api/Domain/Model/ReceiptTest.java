package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptTest {

    ObjectMapper objectMapper;
    Receipt receipt;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        receipt = new Receipt();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("receiptJsonVal")
    void receiptJson(String customerEmailAddress, long id, Date createdAt, BigInteger total, String productDescription, String pdfLocation, String expectedOutput) throws JsonProcessingException {
        // Create customer object
        Customer customer = new Customer();
        customer.setEmailAddress(customerEmailAddress);

        // Create receipt object
        Receipt receipt = new Receipt();
        receipt.setId(id);
        receipt.setCreatedAt(createdAt);
        receipt.setTotal(total);
        receipt.setProductDescription(productDescription);
        receipt.setSales(new ArrayList<>());
        receipt.setCustomer(customer);
        receipt.setPdfLocation(pdfLocation);

        // Create json and perform assertion
        String actualOutput = objectMapper.writeValueAsString(receipt);
        assertEquals(expectedOutput, actualOutput);
    }

    static Stream<Arguments> receiptJsonVal() {
        return Stream.of(
                Arguments.of("a@a.com", 111, new Date(120, Calendar.JANUARY, 1), BigInteger.valueOf(100), "Product Description 1", "a:/",
                        "{\"createdAt\":1577836800000,\"customer\":{\"dateOfBirth\":null,\"emailAddress\":\"a@a.com\",\"id\":null,\"stripeId\":null},\"id\":111,\"pdfLocation\":\"a:/\",\"productDescription\":\"Product Description 1\",\"sales\":[],\"total\":100}"),
                Arguments.of("b@b.com", 222, new Date(121, Calendar.FEBRUARY, 2), BigInteger.valueOf(200), "Product Description 2", "b:/",
                        "{\"createdAt\":1612224000000,\"customer\":{\"dateOfBirth\":null,\"emailAddress\":\"b@b.com\",\"id\":null,\"stripeId\":null},\"id\":222,\"pdfLocation\":\"b:/\",\"productDescription\":\"Product Description 2\",\"sales\":[],\"total\":200}"),
                Arguments.of("c@c.com", 333, new Date(122, Calendar.MARCH, 3), BigInteger.valueOf(300), "Product Description 3", "c:/",
                        "{\"createdAt\":1646265600000,\"customer\":{\"dateOfBirth\":null,\"emailAddress\":\"c@c.com\",\"id\":null,\"stripeId\":null},\"id\":333,\"pdfLocation\":\"c:/\",\"productDescription\":\"Product Description 3\",\"sales\":[],\"total\":300}"),
                Arguments.of("d@d.com", 444, new Date(123, Calendar.APRIL, 4), BigInteger.valueOf(400), "Product Description 4", "d:/",
                        "{\"createdAt\":1680562800000,\"customer\":{\"dateOfBirth\":null,\"emailAddress\":\"d@d.com\",\"id\":null,\"stripeId\":null},\"id\":444,\"pdfLocation\":\"d:/\",\"productDescription\":\"Product Description 4\",\"sales\":[],\"total\":400}"),
                Arguments.of("e@e.com", 555, new Date(124, Calendar.MAY, 5), BigInteger.valueOf(500), "Product Description 5", "e:/",
                        "{\"createdAt\":1714863600000,\"customer\":{\"dateOfBirth\":null,\"emailAddress\":\"e@e.com\",\"id\":null,\"stripeId\":null},\"id\":555,\"pdfLocation\":\"e:/\",\"productDescription\":\"Product Description 5\",\"sales\":[],\"total\":500}")
        );
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
