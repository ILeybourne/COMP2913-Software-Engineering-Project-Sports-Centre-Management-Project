package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Resource resource = new Resource();
        resource.setId(new Long(1));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getResources() throws Exception {
        this.mockMvc.perform(get("/resources/")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void indexResource() throws Exception {
        mockMvc.perform(get("/resources/", 1))
            .andExpect(status().isOk());
    }


    @Test
    void createResource() throws Exception {
        mockMvc.perform(post("/resources/")
                .contentType("application/json")
                //.param("", "")
                .content(objectMapper.writeValueAsBytes(new Resource())))
                .andExpect(status().isOk());
    }

    @Test
    void updateResource() throws Exception {
        mockMvc.perform(put("/resources/{resource_id}", 1)
                .contentType("application/json")
                //.param("", "")
                .content(objectMapper.writeValueAsBytes(new Resource())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteResource() throws Exception {
        mockMvc.perform(delete("/resources/{resource_id}", 1)
                .contentType("application/json"))
                //.param("", "")
                .andExpect(status().isOk());
    }
}