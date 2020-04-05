package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

    @GetMapping
    public CollectionModel<Booking> getBookings() {
        List <Booking> allBookings = bookingService.findAll();
        for (Booking booking : allBookings) {
            Long bookingId = booking.getId();
            Link selfLink = linkTo(BookingController.class).slash(bookingId).withSelfRel();
            Link deleteLink = linkTo(BookingController.class).slash("delete").slash(bookingId).withRel("Delete");
            Link updateLink = linkTo(BookingController.class).slash(bookingId).withRel("Update");
            booking.add(selfLink, deleteLink, updateLink);
               if (booking.getRegularSession()!=null){
                Long activityId = booking.getActivity().getId();
                Long accountId = booking.getAccount().getId();
                Link cancelRegularSessionLink = linkTo(BookingController.class).slash("cancel").slash(activityId).slash(accountId).withRel("Unsubscribe from Regular Session");
                booking.add(cancelRegularSessionLink);
               }
        }

        Link viewAllBookings = linkTo(BookingController.class).withSelfRel();
        CollectionModel<Booking> result = new CollectionModel<>(allBookings, viewAllBookings);
        return result;
    }

    @GetMapping("/{booking_id}")
    public Booking getBookings(@PathVariable long booking_id) {
        return bookingService.findById(booking_id);
    }

    @GetMapping("/account/{account_id}")
    public List<Booking> getBookingsByAccount(@PathVariable long account_id) {
        return bookingRepository.findByAccountId(account_id);
    }

    @GetMapping("/activity/{activity_id}")
    public List<Booking> getBookingsByActivity(@PathVariable long activity_id) {
        return bookingRepository.findByActivityId(activity_id);
    }

    @PostMapping("")
    public Booking createBooking(@Valid @RequestBody Booking booking) {
        return bookingService.save(booking);
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

    @DeleteMapping("/delete/{booking_id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            bookingRepository.delete(booking);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));
    }
}
