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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ActivityTypeControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    ActivityType activityType = new ActivityType();
    activityType.setId(new Long(1));
    Resource resource = new Resource();
    resource.setId(new Long(1));
    activityType.setResource(resource);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getActivityTypes() throws Exception {
    this.mockMvc.perform(get("/activitytypes/")
        .contentType("application/json"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @Disabled
  void getActivitiesByResource() throws Exception {
    this.mockMvc.perform(get("/activitytypes/resource/",1)
        .contentType("application/json"))
        .andExpect(status().isOk())
        .andDo(print());
  }


  @Test
  @Disabled
  void addActivityType() throws Exception {
    mockMvc.perform(post("/activitytypes/",1)
        .contentType("application/json")
        //.param("", "")
        .content(objectMapper.writeValueAsBytes(new Resource())))
        .andExpect(status().isOk());
  }

  @Test
  @Disabled
  void updateActivityType() throws Exception {
    mockMvc.perform(put("/activitytypes/", 1)
        .contentType("application/json")
        //.param("", "")
        .content(objectMapper.writeValueAsBytes(new Resource())))
        .andExpect(status().isOk());
  }

  @Test
  @Disabled
  void deleteActivityTypes() throws Exception {
    mockMvc.perform(delete("/resources/{resource_id}", 1)
        .contentType("application/json"))
        //.param("", "")
        .andExpect(status().isOk());
  }
}