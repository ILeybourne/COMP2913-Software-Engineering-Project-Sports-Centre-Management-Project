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

import java.util.Collection;
import java.util.List;
import java.util.logging.LogManager;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
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
    @Operation(summary = "Get a list of all bookings",
            description = "Get list of all bookings with basic information, self link provides more details")
    public CollectionModel<Booking> getBookings() {
        List <Booking> allBookings = bookingService.findAll();
        for (Booking booking : allBookings) {
            Long bookingId = booking.getId();
            Link selfLink = linkTo(BookingController.class).slash(bookingId).withSelfRel();
            Link accountLink = linkTo(AccountController.class).slash(booking.getAccount().getId()).withRel("Account");
            Link activityLink = linkTo(ActivityController.class).slash(booking.getActivity().getId()).withRel("Activity");
            booking.add(selfLink, accountLink, activityLink);
        }
        Link viewAllBookings = linkTo(BookingController.class).withSelfRel();
        CollectionModel<Booking> result = new CollectionModel<>(allBookings, viewAllBookings);
        return result;
    }

    @GetMapping("/{booking_id}")
    @Operation(summary = "Get a specific booking",
            description = "Get a specific booking with more details/links")
    public Booking getBookingById(@PathVariable Long booking_id) {
        Booking booking = bookingService.findById(booking_id);
        Link deleteLink = linkTo(BookingController.class).slash("delete").slash(booking_id).withRel("Delete");
        Link updateLink = linkTo(BookingController.class).slash(booking_id).withRel("Update");
        //Link receiptLink = linkTo(ReceiptController.class).slash(booking.getReceipt().getId()).slash("receipt").withRel("Receipt");
        Link accountLink = linkTo(AccountController.class).slash(booking.getAccount().getId()).withRel("Account");
        Link activityLink = linkTo(ActivityController.class).slash(booking.getActivity().getId()).withRel("Activity");
        booking.add(deleteLink, updateLink, accountLink, activityLink);
        if (booking.getRegularSession()!=null){
            Link cancelRegularSessionLink = linkTo(BookingController.class).slash("cancel").slash(booking.getActivity().getId()).slash(booking.getAccount().getId()).withRel("Unsubscribe from Regular Session");
            booking.add(cancelRegularSessionLink);
        }
        return booking;
    }

    @GetMapping("/account/{account_id}")
    @Operation(summary = "Get a list of bookings made by a specific account",
            description = "returns a list of bookings placed by a specific account")
    public List<Booking> getBookingsByAccount(@PathVariable Long account_id) {
        return bookingRepository.findByAccountId(account_id);
    }

    @GetMapping("/activity/{activity_id}")
    @Operation(summary = "Get a list of bookings for a specific activity",
            description = "Get list of bookings for a specific activity")
    public CollectionModel<Booking> getBookingsByActivity(@PathVariable Long activity_id) {
        List<Booking> activityBookings = bookingRepository.findByActivityId(activity_id);
        for (Booking booking : activityBookings) {
            Long bookingId = booking.getId();
            Link selfLink = linkTo(BookingController.class).slash(bookingId).withSelfRel();
            booking.add(selfLink);
        }
        Link selfLink = linkTo(ActivityController.class).slash("activity").slash(activity_id).withSelfRel();
        Link activityLink = linkTo(ActivityController.class).slash(activity_id).withRel("Activity");
        CollectionModel<Booking> result = new CollectionModel<>(activityBookings, selfLink, activityLink);
        return result;
    }

    //Post booking with the option to book on to a regular session
    @PostMapping("/{activity_id}")
    @Operation(summary = "Place booking",
            description = "Place a booking for a particular activity with the option to make it into a repeating booking" +
                    "if the activity is a regular session #21 #4")
    public Booking createBooking(@Valid @RequestBody BookingDTO booking, @PathVariable Long activity_id) {
        Booking b = new Booking();
        boolean regularBooking = booking.isRegularBooking();
        Long account_id = booking.getAccountId();
        b.setParticipants(booking.getParticipants());
        return bookingService.createNewBookingForActivity(b, activity_id, account_id, regularBooking);
    }

    @PutMapping("/{booking_id}")
    @Operation(summary = "Update Booking",
            description = "Edit a booking")
    public Booking updateBooking(@PathVariable Long booking_id, @Valid @RequestBody Booking bookingRequest) {
        return bookingRepository.findById(booking_id).map(booking -> {
            booking.setActivity(bookingRequest.getActivity());
            booking.setAccount(bookingRequest.getAccount());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
    }

    //unsubscribe from regular session auto bookings
    @PutMapping("/cancel/{activity_id}/{account_id}")
    @Operation(summary = "unsubscribe from a regular session",
            description = "stop repeating bookings for a regular session")
    public void cancelRegularSessionBooking(@PathVariable Long activity_id,
                                            @PathVariable Long account_id) {
        bookingService.cancelRegularSession(activity_id, account_id);
    }

    @DeleteMapping("/delete/{booking_id}")
    @Operation(summary = "Cancel Booking #5",
            description = "Cancel a booking #5")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            bookingRepository.delete(booking);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));
    }
}
