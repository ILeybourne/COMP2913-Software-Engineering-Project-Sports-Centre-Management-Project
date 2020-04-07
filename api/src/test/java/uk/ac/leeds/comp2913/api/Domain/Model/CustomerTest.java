package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void addReceipt(int id) {
        // Create receipt
        Receipt receipt = new Receipt();
        receipt.setId(id);

        // Add receipt to customer
        customer.addReceipt(receipt);

        // Check receipt is in customer list
        assertTrue(customer.getReceipts().contains(receipt));

        // Check customer is attached to receipt
        assertEquals(customer, receipt.getCustomer());
    }
}
