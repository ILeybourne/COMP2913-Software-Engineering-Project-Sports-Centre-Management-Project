package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ActivityService activityService;

    @MockBean
    ActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    // TODO Failing due to "Page must not be null!" issue
    void getActivities() throws Exception {
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
        when(activityService.getActivities(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/activities")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getActivityByActivityId() throws Exception {
        // Create activity
        Activity activity = new Activity();
        activity.setId(1L);

        // Tie response to service
        when(activityService.findActivityById(any())).thenReturn(activity);

        // Perform get and assert
        mockMvc.perform(get("/activities/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getActivitiesByResourceId() throws Exception {
        // Create resource
        Resource resource = new Resource();
        resource.setId(4L);

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

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Activity> response = new PageImpl<>(List.of(activity1, activity2, activity3), request, 1);

        // Tie response to service
        when(activityService.findByResourceId(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/activities/resource/4")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_create:activity"})
    void createActivity() throws Exception {
        // Create activity
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Test Activity");

        // Create activity
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(1L);
        activityDTO.setName("Test Activity");
        activityDTO.setRegularSession(false);
        activityDTO.setSocial(false);
        activityDTO.setStartTime(new Date(120, Calendar.APRIL, 19));
        activityDTO.setEndTime(new Date(120, Calendar.APRIL, 19));
        activityDTO.setInterval(null);

        // Tie response to service
        when(activityService.createNewActivity(any(), any(), any())).thenReturn(activity);

        // Perform put and assert
        mockMvc.perform(post("/activities/activitytype/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(activityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Disabled
    @Test
    // TODO Talk to Kirsty
    void updateActivity() throws Exception {
        // Create resource
        Resource resource = new Resource();
        resource.setId(2L);

        // Create activity type
        ActivityType activityType = new ActivityType();
        activityType.setId(3);
        activityType.setName("Test AT");
        activityType.setCost(BigDecimal.valueOf(10));
        activityType.setTotalCapacity(20);

        // Create activity
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Test Activity");
        activity.setCost(BigDecimal.valueOf(10));
        activity.setStartTime(new Date(120, Calendar.APRIL, 19));
        activity.setEndTime(new Date(120, Calendar.APRIL, 20));
        activity.setCurrentCapacity(20);
        activity.setResource(resource);
        activity.setActivityType(activityType);

        // Tie response to service
        when(activityService.editActivity(any(), any())).thenReturn(activity);
        when(activityRepository.findById(eq(1L))).thenReturn(Optional.of(activity));
        when(activityRepository.save(activity)).thenReturn(activity);

        // Perform put and assert
        mockMvc.perform(put("/activities/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Disabled
    @Test
    void deleteActivity() {
        // TODO (@SebGarwood) Fix return type
    }

    @Disabled
    @Test
    void deleteRegularSession() {
        // TODO (@SebGarwood) Fix return type
    }
}