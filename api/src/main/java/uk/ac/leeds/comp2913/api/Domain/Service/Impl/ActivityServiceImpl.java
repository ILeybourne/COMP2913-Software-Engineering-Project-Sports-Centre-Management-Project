package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.transaction.Transactional;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ActivityServiceImpl implements ActivityService {
  private final ActivityRepository activityRepository;
  private final RegularSessionRepository regularSessionRepository;
  private final BookingRepository bookingRepository;

  @Autowired
  public ActivityServiceImpl(ActivityRepository activityRepository, RegularSessionRepository regularSessionRepository, BookingRepository bookingRepository) {
    this.activityRepository = activityRepository;
    this.regularSessionRepository = regularSessionRepository;
    this.bookingRepository = bookingRepository;
  }

  @Override
  public Page<Activity> getActivities(Pageable pageable) {
    return activityRepository.findAll(pageable);
  }

  @Override
  public ActivityDTO getCapacityForActivityId(Long activityId) {
    return activityRepository.calculateCurrentCapacity(activityId);
  }

  //*TODO daiy*/
  @Scheduled(cron = "0 0 1 * * MON") //Every Monday at 1am
  //@Scheduled(fixedRate = 5000)
  //Transactional
  public void schedule() {
    // // Log to stdout
    List<RegularSession> regularSessions = regularSessionRepository.findAll(); //Find all currently running regular sessions
    List<Activity> last_scheduled_activities = activityRepository.findAllWithRegularSession(); // custom query in  repo, SELECT MAX(start_time) from activty where repeat_activity_id = ?;

    for (Activity activity : last_scheduled_activities) {
      if (regularSessions.contains(activity.getRegularSession())) {
        Activity new_activity = Activity.createFromLastScheduled(activity);
        this.activityRepository.save(new_activity);

        Set<Booking> last_activity_bookings = activity.getBookings();
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