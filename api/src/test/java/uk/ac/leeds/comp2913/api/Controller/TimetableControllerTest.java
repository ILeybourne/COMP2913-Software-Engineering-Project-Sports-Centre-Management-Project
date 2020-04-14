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

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TimetableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Disabled
    @Test
    // TODO Additional clarification required
    void getTimetable() throws Exception {
        // Create activity
        Activity activity1 = new Activity();
        activity1.setId(1L);

        // Create activity
        Activity activity2 = new Activity();
        activity2.setId(2L);

        // Create activity
        Activity activity3 = new Activity();
        activity3.setId(3L);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Activity> response = new PageImpl<>(List.of(activity1, activity2, activity3), request, 1);

        // Tie response to service
//        when(bookingService.findByAccountId(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/timetable")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    // TODO Additional clarification required
    void getTimetableByResource() throws Exception {
        // Perform get and assert
        mockMvc.perform(get("/timetable")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = "test@comp2913.com")
//    void getResources() throws Exception {
//        mockMvc.perform(get("/timetable")
//                .contentType("application/json"))
//                .andExpect(status().isOk());
//    }
}