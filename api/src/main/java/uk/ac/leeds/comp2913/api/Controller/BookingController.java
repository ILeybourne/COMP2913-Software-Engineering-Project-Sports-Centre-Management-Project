package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.BookingPagedResourcesAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * localhost:8000/swagger-ui.html
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
    private final BookingService bookingService;
    private final PagedResourcesAssembler<Booking> pagedResourcesAssembler;
    private final BookingPagedResourcesAssembler bookingPagedResourcesAssembler;


    @Autowired
    public BookingController( BookingService bookingService, PagedResourcesAssembler<Booking> pagedResourcesAssembler, BookingPagedResourcesAssembler bookingPagedResourcesAssembler) {
        this.bookingService = bookingService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.bookingPagedResourcesAssembler = bookingPagedResourcesAssembler;
    }

     @GetMapping("")
     @Operation(summary = "Get a list of all bookings",
             description = "Get list of all bookings with basic information, self link provides more details")
     public PagedModel<BookingDTO> getBookings(Pageable pageable) {
         return pagedResourcesAssembler.toModel((bookingService.findAll(pageable)), bookingPagedResourcesAssembler);
     }

     @GetMapping("/{booking_id}")
     @Operation(summary = "Get a specific booking",
             description = "Get a specific booking with more details/links")
     public BookingDTO getBookingById(@Parameter(description = "The id of the booking", required = true)@PathVariable Long booking_id) {
        BookingDTO booking = bookingPagedResourcesAssembler.toModel(bookingService.findById(booking_id));
        booking.add(linkTo(BookingController.class).slash(booking_id).withRel("Delete"));
        booking.add(linkTo(BookingController.class).slash(booking_id).withRel("Update"));
        return booking;
     }

     @GetMapping("/account/{account_id}")
     @Operation(summary = "Get a list of bookings made by a specific account",
             description = "returns a list of bookings placed by a specific account")
     public PagedModel<BookingDTO> getBookingsByAccount(Pageable pageable, @Parameter(description = "The ID of the account", required = true)@PathVariable Long account_id) {
         return pagedResourcesAssembler.toModel((bookingService.findByAccountId(pageable, account_id)), bookingPagedResourcesAssembler);
     }

    @GetMapping("/activity/{activity_id}")
    @Operation(summary = "Get a list of bookings for a specific activity",
            description = "Get list of bookings for a specific activity")
    public PagedModel<BookingDTO> getBookingsByActivity(Pageable pageable, @Parameter(description = "The Id of the activity", required = true)@PathVariable Long activity_id) {
           return pagedResourcesAssembler.toModel((bookingService.findByActivityId(pageable, activity_id)), bookingPagedResourcesAssembler);
    }

    //Post booking with the option to book on to a regular session
    @PostMapping("/{activity_id}")
    @Operation(summary = "Place booking",
            description = "Place a booking for a particular activity with the option to make it into a repeating booking" +
                    "if the activity is a regular session #21 #4")
    public Booking createBooking(@Parameter(description = "A Booking DTO object", required = true)@Valid @RequestBody BookingDTO booking,
                                 @Parameter(description = "The activity ID", required = true)@PathVariable Long activity_id) {
        Booking b = new Booking();
        boolean regularBooking = booking.isRegularBooking();
        Long account_id = booking.getAccountId();
        b.setParticipants(booking.getParticipants());
        b.setTransactionId(booking.getTransactionId());
        b.setAmount(booking.getAmount());
        return bookingService.createNewBookingForActivity(b, activity_id, account_id, regularBooking);
    }

    //unsubscribe from regular session auto bookings
    @PutMapping("/cancel/{activity_id}/{account_id}")
    @Operation(summary = "unsubscribe from a regular session",
            description = "stop repeating bookings for a regular session")
    public void cancelRegularSessionBooking(@Parameter(description = "The ID of the regular session activity", required = true)@PathVariable Long activity_id,
                                            @Parameter(description = "The ID of the account booked onto it", required = true)@PathVariable Long account_id) {
        bookingService.cancelRegularSession(activity_id, account_id);
    }

    @DeleteMapping("/{booking_id}")
    @Operation(summary = "Cancel Booking #5",
            description = "Cancel a booking #5")
    public ResponseEntity<?> deleteBooking(@Parameter(description = "The Id of the booking in the path", required = true)@PathVariable Long booking_id) {
        return bookingService.deleteBooking(booking_id);
    }
}
