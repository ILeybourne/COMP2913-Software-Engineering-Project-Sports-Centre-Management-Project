package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipTypeService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MembershipTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MembershipTypeService membershipTypeService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getMembershipTypes() throws Exception {
        // Create membership type
        MembershipType membershipType1 = new MembershipType();
        membershipType1.setId(1L);

        // Create membership type
        MembershipType membershipType2 = new MembershipType();
        membershipType2.setId(2L);

        // Create membership type
        MembershipType membershipType3 = new MembershipType();
        membershipType3.setId(3L);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<MembershipType> response = new PageImpl<>(List.of(membershipType1, membershipType2, membershipType3), request, 1);

        // Tie response to service
        when(membershipTypeService.findAllMembershipTypes(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/membership/types")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void getMembershipTypeById() throws Exception {
        // Create membership type
        MembershipType membershipType = new MembershipType();
        membershipType.setId(1L);

        // Tie response to service
        when(membershipTypeService.findMembershipTypeById(any())).thenReturn(membershipType);

        // Perform get and assert
        mockMvc.perform(get("/membership/types/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}