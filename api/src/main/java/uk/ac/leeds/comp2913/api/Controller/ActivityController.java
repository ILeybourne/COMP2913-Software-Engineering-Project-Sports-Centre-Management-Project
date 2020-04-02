package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ActivityController {

  private final ActivityRepository activityRepository;

  private final ActivityService activityService;

  private final RegularSessionRepository regularSessionRepository;


  @Autowired
  public ActivityController(ActivityRepository activityRepository,  ActivityService activityService, RegularSessionRepository regularSessionRepository) {
    this.activityRepository = activityRepository;
    this.activityService = activityService;
    this.regularSessionRepository = regularSessionRepository;
  }

  /**
   * Get all scheduled activities in the database
   *
   * @param pageable Pagination Metadata
   * @return a page of Activities
   */
  @GetMapping("/activities")
  public Page<Activity> getActivities(Pageable pageable) {
    return activityRepository.findAll(pageable);
  }

  //get scheduled activities by resource ID
  @GetMapping("/resources/{resource_id}/activities")
  public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
    return activityRepository.findByResourceId(resource_id);
  }

  //schedule an activity. Create a one time activity or regular session
  @PostMapping("/activities/{activity_type_id}")
  public Activity createActivity(@Valid @RequestBody ActivityDTO activity, @PathVariable Long activity_type_id) {
    Activity a = new Activity();
    RegularSession regularSession = new RegularSession();
    a.setStartTime(activity.getStartTime());
    a.setEndTime(activity.getEndTime());
    a.setSocial(activity.isSocial());
    if(activity.isRegularSession()){
      regularSession.setInterval(activity.getInterval());
    }else{
      regularSession = null;
    }
    return activityService.createNewActivity(a, activity_type_id, regularSession);
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
          activity.setRegularSession(activityRequest.getRegularSession());

//                    TODO: Others
          return activityRepository.save(activity);
        }).orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID " + activity_id));
  }

  //delete scheduled activity
  @DeleteMapping("/activities/{activity_id}")
  public ResponseEntity<?> deleteActivity(@PathVariable Long activity_id) {
  return activityService.deleteActivity(activity_id);
  }

  //delete regular session, therefore stop activities from repeating
  @DeleteMapping("/activities/cancelregularsession/{regular_session_id}")
  public ResponseEntity<?> deleteRegularSession(@PathVariable Long regular_session_id) {
    try {
      regularSessionRepository.deleteById(regular_session_id);
      return ResponseEntity
          .noContent()
          .build();
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Regular Session not found with that ID " + regular_session_id);
    }
  }
}
