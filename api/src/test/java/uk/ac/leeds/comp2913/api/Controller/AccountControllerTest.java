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

import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Service.AccountService;

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
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setEmailAddress("a@a.com");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAccounts() throws Exception {
        // Create account
        Account account1 = new Account();
        account1.setId(1);
        account1.setCustomer(customer);

        // Create account
        Account account2 = new Account();
        account2.setId(2);
        account2.setCustomer(customer);

        // Create account
        Account account3 = new Account();
        account3.setId(3);
        account3.setCustomer(customer);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Account> response = new PageImpl<>(List.of(account1, account2, account3), request, 1);

        // Tie response to service
        when(accountService.getAccounts(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/account")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getAccountById() throws Exception {
        // Create account
        Account account = new Account();
        account.setId(1);
        account.setCustomer(customer);

        // Tie response to service
        when(accountService.getAccountById(any())).thenReturn(account);

        // Perform get and assert
        mockMvc.perform(get("/account/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}