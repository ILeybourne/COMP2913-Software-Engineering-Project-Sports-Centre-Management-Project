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

//import javafx.scene.chart.Axis;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    Account account = new Account();
    Activity activity = new Activity();

    @BeforeEach
    void setUp() {
        // Create account
        account = new Account();
        account.setId(4);

        // Create activity
        activity = new Activity();
        activity.setId((long) 5);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @WithMockUser(username = "test@comp2913.com")
    void getBookings() throws Exception {
        // Create booking
        Booking booking1 = new Booking();
        booking1.setParticipants(5);
        booking1.setAccount(account);
        booking1.setActivity(activity);

        // Create booking
        Booking booking2 = new Booking();
        booking2.setParticipants(5);
        booking2.setAccount(account);
        booking2.setActivity(activity);

        // Create booking
        Booking booking3 = new Booking();
        booking3.setParticipants(5);
        booking3.setAccount(account);
        booking3.setActivity(activity);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Booking> response = new PageImpl<>(List.of(booking1, booking2, booking3), request, 1);

        // Tie response to service
        when(bookingService.findAll(any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/bookings")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getBookingById() throws Exception {
        // Create booking
        Booking booking = new Booking();
        booking.setId(1);
        booking.setAccount(account);
        booking.setActivity(activity);

        // Tie response to service
        when(bookingService.findById(any())).thenReturn(booking);

        // Perform get and assert
        mockMvc.perform(get("/bookings/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getBookingsByAccount() throws Exception {
        // Create booking
        Booking booking1 = new Booking();
        booking1.setParticipants(5);
        booking1.setAccount(account);
        booking1.setActivity(activity);

        // Create booking
        Booking booking2 = new Booking();
        booking2.setParticipants(5);
        booking2.setAccount(account);
        booking2.setActivity(activity);

        // Create booking
        Booking booking3 = new Booking();
        booking3.setParticipants(5);
        booking3.setAccount(account);
        booking3.setActivity(activity);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Booking> response = new PageImpl<>(List.of(booking1, booking2, booking3), request, 1);

        // Tie response to service
        when(bookingService.findByAccountId(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/bookings/account/4")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void getBookingsByActivity() throws Exception {
        // Create booking
        Booking booking1 = new Booking();
        booking1.setParticipants(5);
        booking1.setAccount(account);
        booking1.setActivity(activity);

        // Create booking
        Booking booking2 = new Booking();
        booking2.setParticipants(5);
        booking2.setAccount(account);
        booking2.setActivity(activity);

        // Create booking
        Booking booking3 = new Booking();
        booking3.setParticipants(5);
        booking3.setAccount(account);
        booking3.setActivity(activity);

        // Create page request and response
        Pageable request = PageRequest.of(0, 10);
        Page<Booking> response = new PageImpl<>(List.of(booking1, booking2, booking3), request, 1);

        // Tie response to service
        when(bookingService.findByActivityId(any(), any())).thenReturn(response);

        // Perform get and assert
        mockMvc.perform(get("/bookings/activity/5")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Disabled
    @Test
    void createBookingValidation() throws Exception {
        // Create booking
        Booking booking = new Booking();
        booking.setId(1);
        booking.setAccount(account);
        booking.setActivity(activity);

        // Create booking dto
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setRegularBooking(false);
        bookingDTO.setParticipants(100);

        // Tie response to service
        when(bookingService.createNewBookingForActivity(any(), any(), any(), any())).thenReturn(booking);

        // Perform post then assert
        mockMvc.perform(post("/bookings/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("100")));
    }

    @Disabled
    @Test
    void cancelRegularSessionBooking() {
        // TODO (@SebGarwood) Fix return type
    }

    @Disabled
    @Test
    void deleteBooking() throws Exception {
        // TODO (@SebGarwood) Fix return type
    }
}