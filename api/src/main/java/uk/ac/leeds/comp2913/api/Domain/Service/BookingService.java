package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

public interface BookingService {
  Booking createNewBookingForActivity(Booking booking, Long activity_id, Long account_id, Boolean regularBooking);

  void cancelRegularSession(Long activity_id, Long account_id);

  List<Booking> findAll();

  Booking findById(Long booking_id);

  Booking save(Booking booking);

  Account findAccountFromBooking(Long booking_id);

  }