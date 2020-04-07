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

    public static final String TENNIS_COURT = "Tennis Court";

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
    void createResourceWithCorrectAuthorities() throws Exception {
        final Resource resource = new Resource();
        resource.setName(TENNIS_COURT);
        when(resourceServiceMock.create(any()))
                .thenReturn(resource);
        mockMvc.perform(post("/resources")
                .with(jwt(jwt -> jwt.subject("ch4mpy").claim("scope", "resource:create")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TENNIS_COURT)));
    }

    @Test
    void createResourceWithoutCorrectAuthorities() throws Exception {
        final Resource resource = new Resource();
        resource.setName(TENNIS_COURT);
        when(resourceServiceMock.create(any()))
                .thenReturn(resource);

        mockMvc.perform(post("/resources")
                .with(jwt(jwt -> jwt.subject("ch4mpy")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(resource)))
                .andExpect(status().isForbidden());
    }

   @Test
   @WithMockUser(username = "test@comp2913.com")
   void getResources() throws Exception {

       Resource resource = new Resource();
       resource.setName(TENNIS_COURT);


       Pageable request = PageRequest.of(0, 10);
       Page<Resource> response = new PageImpl<>(List.of(resource), request, 1);


       when(resourceServiceMock.findAll(any()))
               .thenReturn(response);

       mockMvc.perform(get("/resources")
               .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[*].name", Matchers.contains(TENNIS_COURT)));
   }


    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_update:resource"})
    void updateResource() throws Exception {
        mockMvc.perform(put("/resources/{resource_id}", 1)
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(new Resource())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@comp2913.com", authorities = {"SCOPE_delete:resource"})
    void deleteResource() throws Exception {
        mockMvc.perform(delete("/resources/{resource_id}", 1)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}