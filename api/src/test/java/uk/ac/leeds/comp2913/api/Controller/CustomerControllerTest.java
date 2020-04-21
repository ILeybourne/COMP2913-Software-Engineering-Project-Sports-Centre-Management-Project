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
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCustomers() throws Exception {
        // Create customer
        Customer customer1 = new Customer();
        customer1.setId(1L);

        // Create customer
        Customer customer2 = new Customer();
        customer2.setId(2L);

        // Create customer
        Customer customer3 = new Customer();
        customer3.setId(3L);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Customer> response = new PageImpl<>(List.of(customer1, customer2, customer3), request, 1);

        // Tie response to service
        when(customerService.findAll(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/customer")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {
        // Create customer
        Customer customer = new Customer();
        customer.setId(1L);

        // Tie response to service
        when(customerService.findById(any())).thenReturn(customer);

        // Perform get and assert
        mockMvc.perform(get("/customer/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}