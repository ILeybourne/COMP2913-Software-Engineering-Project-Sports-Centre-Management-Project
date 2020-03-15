package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    void getId() {
        long testId = receipt.getId();
        assertTrue(testId > 0);
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
        String randCost = Double.toString(random.nextDouble());
        BigDecimal testCostSet = new BigDecimal(randCost);
        receipt.setCost(testCostSet);
        BigDecimal testCostGet = receipt.getCost();
        assertEquals(testCostSet, testCostGet);
    }

    @Test
    void setCost() {
        String randCost = Double.toString(random.nextDouble());
        BigDecimal testCostSet = new BigDecimal(randCost);
        receipt.setCost(testCostSet);
        BigDecimal testCostGet = receipt.getCost();
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