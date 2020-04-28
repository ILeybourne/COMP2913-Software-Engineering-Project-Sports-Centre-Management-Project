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

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ActivityTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ActivityTypeService activityTypeService;

    Resource resource;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setId(4L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getActivityTypes() throws Exception {
        // Create activity type
        ActivityType activityType1 = new ActivityType();
        activityType1.setId(1L);
        activityType1.setResource(resource);

        // Create activity type
        ActivityType activityType2 = new ActivityType();
        activityType2.setId(2L);
        activityType2.setResource(resource);

        // Create activity type
        ActivityType activityType3 = new ActivityType();
        activityType3.setId(3L);
        activityType3.setResource(resource);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<ActivityType> response = new PageImpl<>(List.of(activityType1, activityType2, activityType3), request, 1);

        // Tie response to service
        when(activityTypeService.findAll(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/activitytypes")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getActivityTypeId() throws Exception {        // Create activity type
        ActivityType activityType = new ActivityType();
        activityType.setId(1L);
        activityType.setResource(resource);

        // Tie response to service
        when(activityTypeService.findById(any())).thenReturn(activityType);

        // Perform get and assert
        mockMvc.perform(get("/activitytypes/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getActivityTypesByResourceId() throws Exception {
        // Create activity type
        ActivityType activityType1 = new ActivityType();
        activityType1.setId(1L);
        activityType1.setResource(resource);

        // Create activity type
        ActivityType activityType2 = new ActivityType();
        activityType2.setId(2L);
        activityType2.setResource(resource);

        // Create activity type
        ActivityType activityType3 = new ActivityType();
        activityType3.setId(3L);
        activityType3.setResource(resource);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<ActivityType> response = new PageImpl<>(List.of(activityType1, activityType2, activityType3), request, 1);

        // Tie response to service
        when(activityTypeService.findByResourceId(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/activitytypes/resource/4")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void addActivityType() throws Exception {
        // Create activity type
        ActivityType activityType = new ActivityType();
        activityType.setId(1L);
        activityType.setName("Test Activity Type");
        activityType.setResource(resource);

        // Tie response to service
        when(activityTypeService.addActivityType(any(), any())).thenReturn(activityType);

        // Perform post and assert
        mockMvc.perform(post("/activitytypes/resource/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(activityType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateActivityType() throws Exception {
        // Create activity type
        ActivityType activityType = new ActivityType();
        activityType.setId(1L);
        activityType.setName("Test Activity Type");

        // Tie response to service
        when(activityTypeService.updateActivityType(any(), any())).thenReturn(activityType);

        // Perform put and assert
        mockMvc.perform(put("/activitytypes/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(activityType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Disabled
    @Test
    void deleteActivityType() {
        // TODO (@SebGarwood) Fix return type
    }
}