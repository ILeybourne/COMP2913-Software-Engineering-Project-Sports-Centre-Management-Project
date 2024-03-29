package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.NotImplementedException;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipDTO;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MembershipService membershipService;

    Account account;
    MembershipType membershipType;
    boolean repeatingPayment;

    @BeforeEach
    void setUp() {
        // Create account
        account = new Account();
        account.setId(4L);

        // Create membership type
        membershipType = new MembershipType();
        membershipType.setId(5);

        // Set repeating payment
        repeatingPayment = false;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getMembers() throws Exception {
        // Create membership
        Membership membership1 = new Membership();
        membership1.setId(1L);
        membership1.setAccount(account);
        membership1.setMembershipType(membershipType);
        membership1.setRepeatingPayment(repeatingPayment);

        // Create membership
        Membership membership2 = new Membership();
        membership2.setId(2L);
        membership2.setAccount(account);
        membership2.setMembershipType(membershipType);
        membership2.setRepeatingPayment(repeatingPayment);

        // Create membership
        Membership membership3 = new Membership();
        membership3.setId(3L);
        membership3.setAccount(account);
        membership3.setMembershipType(membershipType);
        membership3.setRepeatingPayment(repeatingPayment);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Membership> response = new PageImpl<>(List.of(membership1, membership2, membership3), request, 1);

        // Tie response to service
        when(membershipService.findAllMembers(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/membership/members")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getMembershipById() throws Exception {
        // Create membership
        Membership membership = new Membership();
        membership.setId(1L);
        membership.setAccount(account);
        membership.setMembershipType(membershipType);
        membership.setRepeatingPayment(repeatingPayment);

        // Tie response to service
        when(membershipService.findMembershipById(any())).thenReturn(membership);

        // Perform get and assert
        mockMvc.perform(get("/membership/members/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getMembershipByAccountId() throws Exception {
        // Create membership
        Membership membership1 = new Membership();
        membership1.setId(1L);
        membership1.setAccount(account);
        membership1.setMembershipType(membershipType);
        membership1.setRepeatingPayment(repeatingPayment);

        // Create membership
        Membership membership2 = new Membership();
        membership2.setId(2L);
        membership2.setAccount(account);
        membership2.setMembershipType(membershipType);
        membership2.setRepeatingPayment(repeatingPayment);

        // Create membership
        Membership membership3 = new Membership();
        membership3.setId(3L);
        membership3.setAccount(account);
        membership3.setMembershipType(membershipType);
        membership3.setRepeatingPayment(repeatingPayment);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Membership> response = new PageImpl<>(List.of(membership1, membership2, membership3), request, 1);

        // Tie response to service
        when(membershipService.findMembershipByAccountId(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/membership/members/account/4")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void findAllMembershipsWithType() throws Exception {
        // Create membership
        Membership membership1 = new Membership();
        membership1.setId(1L);
        membership1.setAccount(account);
        membership1.setMembershipType(membershipType);
        membership1.setRepeatingPayment(repeatingPayment);

        // Create membership
        Membership membership2 = new Membership();
        membership2.setId(2L);
        membership2.setAccount(account);
        membership2.setMembershipType(membershipType);
        membership2.setRepeatingPayment(repeatingPayment);

        // Create membership
        Membership membership3 = new Membership();
        membership3.setId(3L);
        membership3.setAccount(account);
        membership3.setMembershipType(membershipType);
        membership3.setRepeatingPayment(repeatingPayment);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Membership> response = new PageImpl<>(List.of(membership1, membership2, membership3), request, 1);

        // Tie response to service
        when(membershipService.findMembershipsByMembershipType(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/membership/members/type/5")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void addMembership() throws Exception {
        // Create membership
        Membership membership = new Membership();
        membership.setId(1L);
        membership.setAccount(account);
        membership.setMembershipType(membershipType);
        membership.setRepeatingPayment(repeatingPayment);

        // Create membership DTO
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setRepeatingPayment(false);
        membershipDTO.setStartDate(new Date(120, Calendar.APRIL, 19));
        membershipDTO.setEndDate(new Date(120, Calendar.APRIL, 20));
        membershipDTO.setAccountId(2L);

        // Tie response to service
        when(membershipService.addMember(any(), any(), any())).thenReturn(membership);

        // Perform post and assert
        mockMvc.perform(post("/membership/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(membershipDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateMembership() throws Exception {
        // Create membership
        Membership membership = new Membership();
        membership.setId(1L);
        membership.setAccount(account);
        membership.setMembershipType(membershipType);
        membership.setRepeatingPayment(repeatingPayment);

        // Tie response to service
        when(membershipService.updateMembership(any(), any())).thenReturn(membership);

        // Perform put and assert
        mockMvc.perform(put("/membership/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(membership)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Disabled
    @Test
    void stopRepeatingPayment() {
        // TODO (@SebGarwood) Fix return type
    }

    @Disabled
    @Test
    void deleteMembership() {
        // TODO (@SebGarwood) Fix return type
    }

}