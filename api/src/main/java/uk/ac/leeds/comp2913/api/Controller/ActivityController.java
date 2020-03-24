package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class ActivityController {

  private final ActivityRepository activityRepository;

  private final ActivityTypeRepository activityTypeRepository;

  private final BookingRepository bookingRepository;

  private final ActivityService activityService;


  @Autowired
  public ActivityController(ActivityRepository activityRepository, ActivityTypeRepository activityTypeRepository, BookingRepository bookingRepository, ActivityService activityService) {
    this.activityRepository = activityRepository;
    this.activityTypeRepository = activityTypeRepository;
    this.bookingRepository = bookingRepository;
    this.activityService = activityService;
  }


  /**
   * Get all scheduled activities in the database
   *
   * @param pageable Pagination Metadata
   * @return a page of Activities
   */
  @GetMapping("/activities")
  public List<ActivityDTO> getActivities(Pageable pageable) {
    List<ActivityDTO> activityDTOList = new LinkedList<>();
    this.activityService.getActivities(pageable).map(activity -> {
      ActivityDTO activityDTO = activityService.getCapacityForActivityId(activity.getId());
      return activityDTO;
    });
    return activityDTOList;
  }

  //get scheduled activities by resource ID
  @GetMapping("/resources/{resource_id}/activities")
  public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
    return activityRepository.findByResourceId(resource_id);
  }


  //schedule an activity
  //Pulls data from activity type, only start and end type is pulled from json
  //need to look at deducting current capacity when bookings are made...
  @PostMapping("/activities")
  public Activity createActivity(@Valid @RequestBody Activity activity) {
    return activityRepository.save(activity);
  }


  //update details of scheduled activity
  @PutMapping("/activities/{activity_id}")
  public Activity updateActivity(@PathVariable Long activity_id, @Valid @RequestBody Activity activityRequest) {

    return activityRepository.findById(activity_id)
        .map(activity -> {
          activity.setName(activityRequest.getName());
          activity.setCost(activityRequest.getCost());
          activity.setStartTime(activityRequest.getStartTime());
          activity.setEndTime(activityRequest.getEndTime());
          activity.setCurrentCapacity(activityRequest.getCurrentCapacity());
//                    TODO: Others
          return activityRepository.save(activity);
        }).orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID " + activity_id));
  }

  //delete scheduled activity
  @DeleteMapping("/activities/{activity_id}")
  public ResponseEntity<?> deleteActivity(@PathVariable Long activity_id) {

    try {
      activityRepository.deleteById(activity_id);
      return ResponseEntity
          .noContent()
          .build();
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Activity not found with that ID " + activity_id);
    }
  }
}
