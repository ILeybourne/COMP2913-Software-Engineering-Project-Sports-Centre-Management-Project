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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;

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
class TimetableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ActivityService activityService;

    Resource resource;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setId(4L);
        resource.setName("Test Resource");
    }

    @AfterEach
    void tearDown() {
    }

    @Disabled
    @Test
    // TODO (@SebGarwood) Fix
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

        // Perform get and assert
        mockMvc.perform(get("/timetable")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("100")));
    }

    @Disabled
    @Test
    // TODO (@SebGarwood) Fix
    void getTimetableByResource() throws Exception {
        // Create activity
        Activity activity1 = new Activity();
        activity1.setId(1L);
        activity1.setResource(resource);

        // Create activity
        Activity activity2 = new Activity();
        activity2.setId(2L);
        activity2.setResource(resource);

        // Create activity
        Activity activity3 = new Activity();
        activity3.setId(3L);
        activity3.setResource(resource);

        // Perform get and assert
        mockMvc.perform(get("/timetable/4")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("100")));
    }
}