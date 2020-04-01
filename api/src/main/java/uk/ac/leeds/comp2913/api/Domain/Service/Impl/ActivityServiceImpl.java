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
  public Page<Activity> getActivities(Pageable pageable) {
    return activityRepository.findAll(pageable);
  }

  //Creates new activity from activityDTO object, activity_type_id is passed through path variable
  @Override
  public Activity createNewActivity(Activity activity, Long activity_type_id, ActivityDTO activityDTO){
    ActivityType activityType = activityTypeRepository.findById(activity_type_id)
        .orElseThrow(() -> new ResourceNotFoundException("Activity type not found for ID" + activity_type_id));

    activity.setActivityType(activityType);
    activity.setStartTime(activityDTO.getStartTime());
    activity.setEndTime(activityDTO.getEndTime());
    activity.setSocial(activityDTO.isSocial());

    //If the activity is regular session then the boolean from activityDTO will be true
    //If true then create a regular session which will allow the activity to be repeated
    if(activityDTO.isRegularSession()){
      RegularSession regularSession = new RegularSession();
      regularSession.setInterval(activityDTO.getInterval());
      regularSessionRepository.save(regularSession);
      activity.setRegularSession(regularSession);
    }
    activityRepository.save(activity);
    return  activity;
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
  //@Scheduled(cron = "0 1 1 * * ?")// Every day at 1:01 am
  //@Scheduled(cron = "0 0 1 * * MON") //Every Monday at 1am
 // @Scheduled(fixedDelay=5000)  // EVERY 5 Seconds: Used for testing
  @Transactional
  public void schedule() {
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
}