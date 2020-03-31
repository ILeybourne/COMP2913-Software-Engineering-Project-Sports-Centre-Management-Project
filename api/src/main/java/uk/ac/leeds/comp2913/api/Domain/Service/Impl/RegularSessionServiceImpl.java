package uk.ac.leeds.comp2913.api.Domain.Service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.RegularSessionService;

import java.util.List;
import java.util.Set;

@Service
public class RegularSessionServiceImpl implements RegularSessionService {
  RegularSessionRepository regularSessionRepository;
  ActivityRepository activityRepository;
  BookingRepository bookingRepository;

  @Autowired
  public RegularSessionServiceImpl(RegularSessionRepository regularSessionRepository, BookingRepository bookingRepository, ActivityRepository activityRepository) {
    this.regularSessionRepository = regularSessionRepository;
    this.bookingRepository = bookingRepository;
    this.activityRepository = activityRepository;
  }

  @Override
  public void createRegularSessionBooking(Long regular_session_id, Booking booking) {
    List<RegularSession> regularSessions= regularSessionRepository.findAll(); //Find all currently running regular sessions
    for (RegularSession regularSession : regularSessions) {
      if (regularSession.getId().equals(regular_session_id)) {
        booking.setRegularSession(regularSession);
        bookingRepository.save(booking);
      }
    }
  }

  @Override
  public void addRegularSessionToActivity(Long activity_id, RegularSession regularSession) {
    List<Activity> activities = activityRepository.findAll(); //Find all currently running regular sessions
    for (Activity activity : activities) {
      if (activity.getId().equals(activity_id)) {
        activity.setRegularSession(regularSession);
        activityRepository.save(activity);
      }
    }
  }

  //Remove regular session from an account
  @Override
  public void cancelRegularSession(Long regular_session_id, Long account_id){
    List<RegularSession> regularSessions = regularSessionRepository.findAll(); //Find all currently running regular sessions
    for (RegularSession regularSession : regularSessions) {
      if (regularSession.getId().equals(regular_session_id)) {
        Set<Booking> bookings = regularSession.getBookings();
        for (Booking booking : bookings) {
          Account account = booking.getAccount();
          if (account.getId()==(account_id)){
            booking.setRegularSession(null);
            bookingRepository.save(booking);
          }
        }
      }
      }
  }


  //custom delete method- foreign key prevents the regular session from being deleted
  //this method loops through bookings and activities to set the regular session to null before deleting
  @Override
  public void deleteRegularSession(Long regular_session_id) {
      List<RegularSession> regularSessions = regularSessionRepository.findAll(); //Find all currently running regular sessions
      for (RegularSession regularSession : regularSessions) {
        if (regularSession.getId().equals(regular_session_id)) {
          Set<Activity> activities = regularSession.getActivities();
          for (Activity activity : activities) {
            activity.setRegularSession(null);
            activityRepository.save(activity);
          }
          Set<Booking> bookings = regularSession.getBookings();
          for (Booking booking : bookings) {
            booking.setRegularSession(null);
            bookingRepository.save(booking);
          }
        }
      }
      regularSessionRepository.deleteById(regular_session_id);
    }
  }
