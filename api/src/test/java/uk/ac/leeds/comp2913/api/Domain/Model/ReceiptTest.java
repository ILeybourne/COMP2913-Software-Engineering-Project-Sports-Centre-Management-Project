package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiptTest {

    Receipt receipt;
    Random random;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        receipt.setCreatedAt(testDateSet);
        Date testDateGet = receipt.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        receipt.setCreatedAt(testDateSet);
        Date testDateGet = receipt.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getCost() {
      final BigInteger testCostSet = BigInteger.valueOf(random.nextInt());
      receipt.setTotal(testCostSet);
        BigInteger testCostGet = receipt.getTotal();
        assertEquals(testCostSet, testCostGet);
    }

    @Test
    void getProductDescription() {
        String testProductDescriptionSet = Integer.toString(random.nextInt());
        receipt.setProductDescription(testProductDescriptionSet);
        String testProductDescriptionGet = receipt.getProductDescription();
        assertEquals(testProductDescriptionSet, testProductDescriptionGet);
    }

    @Test
    void setProductDescription() {
        String testProductDescriptionSet = Integer.toString(random.nextInt());
        receipt.setProductDescription(testProductDescriptionSet);
        String testProductDescriptionGet = receipt.getProductDescription();
        assertEquals(testProductDescriptionSet, testProductDescriptionGet);
    }
}
