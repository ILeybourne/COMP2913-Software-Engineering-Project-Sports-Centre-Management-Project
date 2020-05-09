package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

public interface BookingService {
  Booking createNewBookingForActivity(Booking booking, Long activity_id, Long account_id, Boolean regularBooking) throws IOException, MessagingException;

  Page<Booking> cancelRegularSession(Long activity_id, Long account_id, Pageable pageable);

  Page<Booking> findAll(Pageable pageable);

  Booking findById(Long booking_id);

  Booking save(Booking booking);

  Page<Booking> findByAccountId(Pageable pageable, Long account_id);

  Page<Booking> findByActivityId(Pageable pageable, Long activity_id);

  Page<Booking> findByUsername(Pageable pageable, String username, Boolean isManager);

  Account findAccountFromBooking(Long booking_id);

  Booking updateBooking(Long booking_id, Booking bookingRequest);

  ResponseEntity<?> deleteBooking(Long bookingId);
}