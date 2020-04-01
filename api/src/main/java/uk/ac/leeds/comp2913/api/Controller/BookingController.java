package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/bookings")
public class BookingController {
  private final JavaMailSender javaMailSender;
  private final BookingRepository bookingRepository;
  private final BookingService bookingService;

  @Autowired
  public BookingController(JavaMailSender javaMailSender, BookingRepository bookingRepository, BookingService bookingServiceImpl) {
    this.javaMailSender = javaMailSender;
    this.bookingRepository = bookingRepository;
    this.bookingService = bookingServiceImpl;
  }

  @GetMapping("/send_email")
  public Booking booking() throws MessagingException {

    // To address
    final String EMAIL_ADDRESS = "izzywu@hotmail.com";

    // Subject
    final String EMAIL_SUBJECT = "Test Email";

    // Body (can be HTML)
    final String EMAIL_BODY = "This is a test email.";

    // Name of attached file
    final String EMAIL_ATTACHMENT_NAME = "receipt.pdf";

    // Path of file to attach
    final String EMAIL_ATTACHMENT_PATH = "info/bookings/receipt_123.pdf";

    // Create message object
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    // Set address, subject, and body
    mimeMessageHelper.setTo(EMAIL_ADDRESS);
    mimeMessageHelper.setSubject(EMAIL_SUBJECT);
    mimeMessageHelper.setText(EMAIL_BODY);

    // Add attachment to email
//        mimeMessageHelper.addAttachment(EMAIL_ATTACHMENT_NAME, new File(EMAIL_ATTACHMENT_PATH));

    // Send email
    javaMailSender.send(mimeMessage);
    return null;
  }

  @GetMapping("/{booking_id}")
  public Booking getBookings(@PathVariable long booking_id, Pageable pageable) {
    return bookingRepository.findById(booking_id)
      .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
  }

  @GetMapping("")
  public Page<Booking> getBookings(Pageable pageable) {
    return bookingRepository.findAll(pageable);
  }


  //Post booking with the option to book on to a regular session
  @PostMapping("/{activity_id}/{account_id}")
  public Booking createBooking(@Valid @RequestBody BookingDTO booking, @PathVariable Long activity_id, @PathVariable Long account_id) {
    Booking b = new Booking();
    return bookingService.createNewBookingForActivity(b, activity_id, account_id, booking);
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
