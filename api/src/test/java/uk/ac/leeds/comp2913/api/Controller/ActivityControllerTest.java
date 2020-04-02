package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ActivityControllerTest {

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

    @Test
    @WithMockUser(username = "test@comp2913.com")
    void getActivitiesByResourceId() throws Exception {
        mockMvc.perform(get("activities/resource/{resource_id}", 1)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@comp2913.com")
    void addActivity() throws Exception {
        mockMvc.perform(post("/activities/activitytype/{activity_type_id}", 1)
                .contentType("application/json")
                //.param("", "")
                .content(objectMapper.writeValueAsBytes(new Activity())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@comp2913.com")
    void updateActivity() throws Exception {
        mockMvc.perform(put("activities/{activity_id}", 1)
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(new Activity())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@comp2913.com")
    void deleteActivity() throws Exception {
        /*TODO set up mock*/
        mockMvc.perform(delete("activities/{activity_id}", 1)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}