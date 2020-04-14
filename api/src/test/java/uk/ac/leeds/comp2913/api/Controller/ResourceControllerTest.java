package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockBean
    private ResourceService resourceServiceMock;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_read:resource"})
    void getResources() throws Exception {
        // Create resource
        Resource resource1 = new Resource();
        resource1.setId(1L);

        // Create resource
        Resource resource2 = new Resource();
        resource2.setId(2L);

        // Create resource
        Resource resource3 = new Resource();
        resource3.setId(3L);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Resource> response = new PageImpl<>(List.of(resource1, resource2, resource3), request, 1);

        // Tie response to service
        when(resourceServiceMock.findAll(any())).thenReturn(response);

        // Perform response to service
        mockMvc.perform(get("/resources")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_read:resource"})
    // TODO Failing due to "Page must not be null!" issue
    void getResourceById() throws Exception {
        // Create resource
        Resource resource = new Resource();
        resource.setId(1L);

        // Tie response to service
        when(resourceServiceMock.findById(any())).thenReturn(resource);

        // Perform response to service
        mockMvc.perform(get("/resources")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void createResource() {
        // TODO Additional clarification required
    }

    @Disabled
    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_update:resource"})
    // TODO Failing due to 400 status
    void updateResource() throws Exception {
        // Update resource
        mockMvc.perform(put("/resources/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(new Resource())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_delete:resource"})
    void deleteResource() throws Exception {
        // Delete resource
        mockMvc.perform(delete("/resources/{resource_id}", 1)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_create:resource"})
    // TODO Explanation required for authorities tests
    void createResourceWithCorrectAuthorities() throws Exception {
        // Create resource
        Resource resource = new Resource();
        resource.setId(1L);
        resource.setName("Tennis Court");

        // Tie response to service
        when(resourceServiceMock.create(any())).thenReturn(resource);

        // Perform get and assert
        mockMvc.perform(post("/resources")
                .with(jwt(jwt -> jwt.subject("ch4mpy").claim("scope", "resource:create")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tennis Court")));
    }

    @Disabled
    @Test
    // TODO Explanation required for authorities tests
    void createResourceWithoutCorrectAuthorities() throws Exception {
        // Create resource
        Resource resource = new Resource();
        resource.setId(1L);
        resource.setName("Tennis Court");

        // Tie response to service
        when(resourceServiceMock.create(any())).thenReturn(resource);

        // Perform get and assert
        mockMvc.perform(post("/resources")
                .with(jwt(jwt -> jwt.subject("ch4mpy")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(resource)))
                .andExpect(status().isForbidden());
    }
}