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
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        List<Membership> memberships = new ArrayList<>();
        Membership membership = new Membership();

    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void getMemberships() throws Exception {
        mockMvc.perform(get("/membership/members")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void addMembership() throws Exception {
        mockMvc.perform(post("/membership/",1,1)
            .contentType("application/json")
            //.param("", "")
            .content(objectMapper.writeValueAsBytes(new Resource())))
            .andExpect(status().isOk());
    }

    @Test
    void updateMembership() throws Exception {
        mockMvc.perform(put("/membership/", 1)
            .contentType("application/json")
            //.param("", "")
            .content(objectMapper.writeValueAsBytes(new Resource())))
            .andExpect(status().isOk());
    }

    @Test
    void deleteMembership() throws Exception {
        mockMvc.perform(delete("/membership/", 1)
            .contentType("application/json"))
            //.param("", "")
            .andExpect(status().isOk());
    }
}