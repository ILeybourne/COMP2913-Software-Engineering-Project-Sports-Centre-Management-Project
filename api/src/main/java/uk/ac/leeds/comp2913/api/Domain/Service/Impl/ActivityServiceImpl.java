package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.*;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class ActivityServiceImpl implements ActivityService {
  private final ActivityRepository activityRepository;
  private final RegularSessionRepository regularSessionRepository;
  private final BookingRepository bookingRepository;
  private final ActivityTypeRepository activityTypeRepository;

  @Autowired
  public ActivityServiceImpl(ActivityRepository activityRepository, RegularSessionRepository regularSessionRepository, BookingRepository bookingRepository, ActivityTypeRepository activityTypeRepository) {
    this.activityRepository = activityRepository;
    this.regularSessionRepository = regularSessionRepository;
    this.bookingRepository = bookingRepository;
    this.activityTypeRepository =activityTypeRepository;
  }

  @Override
  public List<Activity> getActivities(){
    return activityRepository.findAll();
  }

  @Override
  public Activity findActivityById(Long activity_id){
    return activityRepository.findById(activity_id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity not found for ID" + activity_id));
  }

  @Override
  public  List<Activity> findByResourceId(Long resource_id){
    return activityRepository.findByResourceId(resource_id);
  }

  //Creates new activity from activityDTO object, activity_type_id is passed through path variable
  @Override
  @Transactional
  public Activity createNewActivity(Activity activity, Long activity_type_id, RegularSession regularSession){
    ActivityType activityType = activityTypeRepository.findById(activity_type_id)
        .orElseThrow(() -> new ResourceNotFoundException("Activity type not found for ID" + activity_type_id));

    activity.setActivityType(activityType);

    if (regularSession != null){
      regularSessionRepository.save(regularSession);
      activity.setRegularSession(regularSession);
    }
    activityRepository.save(activity);
    return  activity;
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
   * Scheduled method to automatically create and post regular sessions and their bookings
   * Currently disabled during development
   *
   * When an activity is created, a manager can pass through a boolean as true with an interval to make it a regular session
   * this will create a regular session in the regular session table and sets the foreign key of the activity to the regular session id
   *
   * When a booking is made against an activity that has a regular session as a foreign key, then the customer can pass a boolean as true
   * to make the booking repeat at a reduced rate
   *
   * the bookings and activities will continue to be automatically scheduled/booked as long as the foreign key is not null
   * in the last scheduled activity and the booking in the last scheduled activity
   */

  public void automatedRegularSessionAndBookings(){
    // // Log to stdout
    List<RegularSession> regularSessions = regularSessionRepository.findAll(); //Find all currently running regular sessions
    List<Activity> last_scheduled_activities = activityRepository.findAllWithRegularSession(); // custom query in  repo, SELECT MAX(start_time) from activty where repeat_activity_id = ?;

    for (Activity session : last_scheduled_activities) {
      if (regularSessions.contains(session.getRegularSession())) {
        Activity new_activity = Activity.createFromLastScheduled(session);
        this.activityRepository.save(new_activity);

        Set<Booking> last_activity_bookings = session.getBookings();
        if (last_activity_bookings != null){
          for (Booking booking : last_activity_bookings) {
            if (booking.getRegularSession() != (null)) {
              Booking new_booking = Booking.createBookingFromRegularSession(new_activity, booking);
              this.bookingRepository.save(new_booking);
            }
          }
        }
      }
    }
  }

  public void automatedMembershipRenewals(){}


  //@Scheduled(cron = "0 1 1 * * ?")// Every day at 1:01 am
  //@Scheduled(cron = "0 0 1 * * MON") //Every Monday at 1am
 // @Scheduled(fixedDelay=5000)  // EVERY 5 Seconds: Used for testing
  @Transactional
  public void schedule() {
    automatedRegularSessionAndBookings();
    //automatedMembershipRenewals();
  }
}