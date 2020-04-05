package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.BookingService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

import javax.transaction.Transactional;

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
  public List<Booking> findAll(){
    return bookingRepository.findAll();
  }


  @Override
  public Booking findById(Long booking_id){
    return bookingRepository.findById(booking_id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
}

  @Transactional
  @Override
  public Booking save(Booking booking){
    return bookingRepository.save(booking);
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
