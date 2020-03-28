package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReceiptServiceImplTest {

    @Autowired
    ReceiptService receiptService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void invoice() {
        receiptService.invoice("12345678", new ArrayList<>());
    }
}