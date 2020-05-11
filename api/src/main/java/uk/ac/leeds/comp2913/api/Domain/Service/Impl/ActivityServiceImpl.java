package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.*;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Domain.Service.PaymentService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

import org.springframework.data.domain.PageImpl;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.lang.System.err;

@Service
public class ActivityServiceImpl implements ActivityService {
  private final ActivityRepository activityRepository;
  private final RegularSessionRepository regularSessionRepository;
  private final BookingRepository bookingRepository;
  private final ActivityTypeRepository activityTypeRepository;
  private final PaymentService paymentService;

  @Autowired
  public ActivityServiceImpl(ActivityRepository activityRepository, RegularSessionRepository regularSessionRepository, BookingRepository bookingRepository, ActivityTypeRepository activityTypeRepository, @Lazy PaymentService paymentService) {
    this.activityRepository = activityRepository;
    this.regularSessionRepository = regularSessionRepository;
    this.bookingRepository = bookingRepository;
    this.activityTypeRepository =activityTypeRepository;
    this.paymentService = paymentService;
  }

  @Override
  public Page<Activity> getActivities(Pageable pageable){
    return activityRepository.findAllWithPagination(pageable);
  }

  @Override
  public Activity findActivityById(Long activity_id){
    return activityRepository.findById(activity_id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity not found for ID" + activity_id));
  }

  @Override
  public  Page<Activity> findByResourceId(Pageable pageable, Long resource_id){
    return activityRepository.findByResourceId(pageable, resource_id);
  }

  @Override
  public Page<Activity> findAllWithResources(Pageable pageable){
    List<Activity> activitiesWithResources = activityRepository.findAllWithResources();
    Page<Activity> activities = new PageImpl<>(activitiesWithResources, pageable, activitiesWithResources.size());
    return activities;
  }


  //Creates new activity from activityDTO object, activity_type_id is passed through path variable
  @Override
  @Transactional
  public Activity createNewActivity(Activity activity, Long activity_type_id, RegularSession regularSession){
    ActivityType activityType = activityTypeRepository.findById(activity_type_id)
        .orElseThrow(() -> new ResourceNotFoundException("Activity type not found for ID" + activity_type_id));
    activity.setActivityType(activityType);
    if (regularSession != null){
      this.regularSessionRepository.save(regularSession);
      activity.setRegularSession(regularSession);
    }
    activityRepository.save(activity);
    return activityRepository.findById(activity.getId())
            .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional
  public Activity editActivity(Long activity_id, Activity activityRequest){
    Activity activity = activityRepository.findById(activity_id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID " + activity_id));
    activity.setName(activityRequest.getName());
    activity.setCost(activityRequest.getCost());
    activity.setStartTime(activityRequest.getStartTime());
    activity.setEndTime(activityRequest.getEndTime());
    activity.setCurrentCapacity(activityRequest.getCurrentCapacity());
    activity.setRegularSession(activityRequest.getRegularSession());
    return activityRepository.save(activity);
  }

  //Delete activity from timetable
  @Override
  @Transactional
  public ResponseEntity<?> deleteActivity(Long activity_id) {
    try {
      activityRepository.deleteById(activity_id);
      return ResponseEntity
          .noContent()
          .build();
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Activity not found with that ID " + activity_id);
    }
  }

  @Override
  @Transactional
  public ResponseEntity<?> deleteRegularSession(Long regular_session_id){
    try {
      regularSessionRepository.deleteById(regular_session_id);
      return ResponseEntity
              .noContent()
              .build();
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Regular Session not found with that ID " + regular_session_id);
    }
  }

  /**
   * When an activity is created, a manager can pass through a boolean as true with an interval to make it a regular session
   * this will create a regular session in the regular session table and sets the foreign key of the activity to the regular session id
   *
   * When a booking is made against an activity that has a regular session as a foreign key, then the customer can pass a boolean as true
   * to make the booking repeat at a reduced rate
   *
   * the bookings and activities will continue to be automatically scheduled/booked as long as the foreign key is not null
   * in the last scheduled activity and the booking in the last scheduled activity
   *
   * activities and bookings will only be posted when their last scheduled activity has taken place. This is to prevent users from
   * booking onto regular sessions that is not the last scheduled one, otherwise the below solution would not repeat their bookings
   *
   * SUGGESTION: timetable view front end, earliest viewable activities from today
   */
  @Override
  public void automatedRegularSessionAndBookings() throws StripeException {
    // // Log to stdout
    List<Activity> last_scheduled_activities = activityRepository.findAllWithRegularSession(); // custom query in  repo, SELECT MAX(start_time) from activty where repeat_activity_id = ?;
    for (Activity session : last_scheduled_activities) {
      Date now = new Date(); //Get the date now

      //The next session will not be posted until the previous one has taken place
      while(session.getStartTime().before(now)){ //So only one regular session is available for the customer to book on to (otherwise autobookings would not work)
        Activity new_activity = Activity.createFromLastScheduled(session);
        this.activityRepository.save(new_activity);
        Set<Booking> last_activity_bookings = session.getBookings();
        if (last_activity_bookings != null){
          for (Booking booking : last_activity_bookings) {
            if (booking.getRegularSession() != (null)) {
              Booking new_booking = Booking.createBookingFromRegularSession(new_activity, booking);
              Customer customer = new_booking.getAccount().getCustomer();
              try {
                PayResponseBodyDTO paymentResponseDTO = paymentService.createFromSavedCard(customer.getId(), customer.getEmailAddress(), new_booking.getAmount(), false, 0);
                new_booking.setTransactionId(paymentResponseDTO.getTransactionId());
                this.bookingRepository.save(new_booking);
              }catch (CardException err){
              }
            }
          }
        }
        session = new_activity;
      }
    }
  }
}