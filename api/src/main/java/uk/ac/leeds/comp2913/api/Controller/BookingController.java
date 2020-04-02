package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;


import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service
 * TODO: @CHORE, add HAL to all endpoints, with links to where the client can find
 * *          the associated resource, account and activity  for the booking
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 * <p>
 * TODO: @FEATURE, send booking confirmation email on creation/change
 * TODO: @FEATURE, handle Stripe
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingRepository bookingRepository, BookingService bookingServiceImpl) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingServiceImpl;
    }

    @GetMapping("")
    public Page<Booking> getBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @GetMapping("/{booking_id}")
    public Booking getBookings(@PathVariable long booking_id, Pageable pageable) {
        return bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
    }

    @PostMapping("")
    public Booking createBooking(@Valid @RequestBody Booking booking) {
        return bookingRepository.save(booking);
    }

    @GetMapping("")
    public Page<Booking> getBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }


    //Post booking with the option to book on to a regular session
    @PostMapping("/{activity_id}/{account_id}")
    public Booking createBooking(@Valid @RequestBody BookingDTO booking, @PathVariable Long activity_id, @PathVariable Long account_id) {
        Booking b = new Booking();
        boolean regularBooking = booking.isRegularBooking();
        b.setParticipants(booking.getParticipants());
        return bookingService.createNewBookingForActivity(b, activity_id, account_id, regularBooking);
    }

    @PutMapping("/{booking_id}")
    public Booking updateBooking(@PathVariable Long booking_id, @Valid @RequestBody Booking bookingRequest) {
        return bookingRepository.findById(booking_id).map(booking -> {
            booking.setActivity(bookingRequest.getActivity());
            booking.setAccount(bookingRequest.getAccount());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
    }

    //unsubscribe from regular session auto bookings
    @PutMapping("/cancel/{activity_id}/{account_id}")
    public void cancelRegularSessionBooking(@PathVariable Long activity_id,
                                            @PathVariable Long account_id) {
        bookingService.cancelRegularSession(activity_id, account_id);
    }

    @DeleteMapping("/{booking_id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            bookingRepository.delete(booking);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));
    }
}
