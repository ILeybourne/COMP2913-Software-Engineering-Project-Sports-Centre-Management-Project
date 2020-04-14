package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ReceiptService receiptService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_customer"})
    void getReceipts() throws Exception {
        // Create receipt
        Receipt receipt1 = new Receipt();
        receipt1.setId(1L);

        // Create receipt
        Receipt receipt2 = new Receipt();
        receipt2.setId(2L);

        // Create receipt
        Receipt receipt3 = new Receipt();
        receipt3.setId(3L);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Receipt> response = new PageImpl<>(List.of(receipt1, receipt2, receipt3), request, 1);

        // Tie response to service
        when(receiptService.findAll(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/receipts")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_customer"})
    void getReceipt() throws Exception {
        // Create receipt
        Receipt receipt = new Receipt();
        receipt.setId(1L);

        // Tie response to service
        when(receiptService.findById(any())).thenReturn(receipt);

        // Perform get and assert
        mockMvc.perform(get("/receipts/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void deleteReceipt() {
        // TODO (@SebGarwood) Fix return type
    }
}