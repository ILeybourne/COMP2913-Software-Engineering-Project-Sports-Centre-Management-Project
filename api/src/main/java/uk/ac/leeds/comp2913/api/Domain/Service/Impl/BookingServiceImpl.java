package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
public class BookingServiceImpl implements BookingService {

  private final ActivityRepository activityRepository;
  private final RegularSessionRepository regularSessionRepository;
  private final BookingRepository bookingRepository;
  private AccountRepository accountRepository;

  @Autowired
  public BookingServiceImpl(RegularSessionRepository regularSessionRepository, BookingRepository bookingRepository, ActivityRepository activityRepository, AccountRepository accountRepository) {
    this.regularSessionRepository = regularSessionRepository;
    this.bookingRepository = bookingRepository;
    this.activityRepository = activityRepository;
    this.accountRepository = accountRepository;
  }

  @Override
  public Page<Booking> findAll(Pageable pageable){
    return bookingRepository.findAll(pageable);
  }

  @Override
  public Page<Booking> findByAccountId(Pageable pageable, Long account_id){
    return bookingRepository.findByAccountId(pageable, account_id);
  }

  @Override
  public Page<Booking> findByActivityId(Pageable pageable, Long activity_id){
    return bookingRepository.findByActivityId(pageable, activity_id);
  }

  @Override
  @Transactional
  public Booking findById(Long booking_id){
    return bookingRepository.findById(booking_id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
}

  @Override
  @Transactional
  public Account findAccountFromBooking(Long booking_id){
    Booking booking = bookingRepository.findById(booking_id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
    Account account = booking.getAccount();
    Hibernate.initialize(account);
    return account;
  }

  @Transactional
  @Override
  public Booking save(Booking booking){
    return bookingRepository.save(booking);
  }

  @Transactional
  @Override
  public Booking updateBooking(Long booking_id, Booking bookingRequest) {
    Booking booking =bookingRepository.findById(booking_id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
    booking.setActivity(bookingRequest.getActivity());
    booking.setAccount(bookingRequest.getAccount());
    booking.setRegularSession(bookingRequest.getRegularSession());
    booking.setParticipants(bookingRequest.getParticipants());
      return bookingRepository.save(booking);
  }

  @Transactional
  @Override
  public ResponseEntity<?> deleteBooking(Long booking_id) {
    return bookingRepository.findById(booking_id).map(booking -> {
      bookingRepository.delete(booking);
      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
  }












  //Posts a new booking for an activity. The customer can pass a boolean to create automatic bookings for repeating sessions
  //at a reduced rate
  @Override
  public Booking createNewBookingForActivity(Booking booking, Long activity_id, Long account_id, Boolean regularBooking){
    Activity a = activityRepository.findById(activity_id)
        .orElseThrow(() -> new ResourceNotFoundException("Activity not found for ID" + activity_id));

    Account account = accountRepository.findById(account_id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));
    booking.setAccount(account);
    booking.setActivity(a);
    if(regularBooking){
      booking.setRegularSession(a.getRegularSession());
    }
    booking.setAmount(a.getCost());
    bookingRepository.save(booking);
    return  booking;
  }

  //Cancels a repeating booking
  //Uses the Query in custom implementation
  //The user can click on any activity that is repeating, the below method will take the regular session from the activity
  //which will then be input into the query with their account id, so all bookings for the regular session against their account
  //is turned to null.
  @Transactional
  @Override
  public void cancelRegularSession(Long activity_id, Long account_id){
    Activity a = activityRepository.findById(activity_id)
        .orElseThrow(() -> new ResourceNotFoundException("Activity not found for ID" + activity_id));
    Long regular_session_id = a.getRegularSession().getId();
    bookingRepository.unsubscribeFromRegularSession(regular_session_id, account_id);
  }

}
