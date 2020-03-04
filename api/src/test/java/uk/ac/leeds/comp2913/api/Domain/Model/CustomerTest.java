package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    Random random;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    void userLogin() {
        // TODO Not implemented
    }
}
