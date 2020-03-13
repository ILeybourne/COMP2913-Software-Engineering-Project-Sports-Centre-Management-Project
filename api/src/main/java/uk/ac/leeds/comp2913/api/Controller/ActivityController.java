package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@RestController
@CrossOrigin(value = "http://localhost:8080")
public class ActivityController {

    private final ActivityRepository activityRepository;

    private final ResourceRepository resourceRepository;

    private final ActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityController(ActivityRepository activityRepository, ResourceRepository resourceRepository, ActivityTypeRepository activityTypeRepository) {
      this.activityRepository = activityRepository;
      this.resourceRepository = resourceRepository;
      this.activityTypeRepository = activityTypeRepository;
    }


  /**
   * Get all scheduled activities in the database
   *
   * @param pageable
   * @return
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

    //schedule an activity
    //Pulls data from activity type, only start and end type is pulled from json
    //need to look at deducting current capacity when bookings are made...
    @PostMapping("/activities/{activity_type_id}/activities")
    public Activity AddActivity(@PathVariable Long activity_type_id, @Valid @RequestBody Activity activity) {
      return activityTypeRepository.findById(activity_type_id)
                .map(activityType -> {
                  activity.setCost(activityType.getCost());
                  activity.setCurrentCapacity(activityType.getTotalCapacity());
                  activity.setName(activityType.getName());
                  activity.setResource(activityType.getResource());
                  activity.setStartTime(activity.getStartTime());
                  activity.setEndTime(activity.getEndTime());
                  return activityRepository.save(activity);
                }).orElseThrow(() -> new ResourceNotFoundException("Activity Type not found with id " + activity_type_id));
    }

    //update details of scheduled activity
    @PutMapping("/activities/{activity_id}")
    public Activity updateActivity(@PathVariable Long activity_id, @Valid @RequestBody Activity activityRequest) {

        return activityRepository.findById(activity_id)
                .map(activity -> {
                    activity.setName(activityRequest.getName());
                    activity.setStartTime(activityRequest.getStartTime());
                    activity.setEndTime(activityRequest.getEndTime());
                    activity.setTotalCapacity(activityRequest.getTotalCapacity());
                    activity.setCurrentCapacity(activityRequest.getCurrentCapacity());
//                    TODO: Others
                  return activityRepository.save(activity);
                }).orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID " + activity_id));
    }

    //delete activity in timetable
    @DeleteMapping("/activities/{activity_id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long activity_id) {

      try {
        activityRepository.deleteById(activity_id);
        return ResponseEntity
          .noContent()
          .build();
      } catch (EmptyResultDataAccessException e){
        throw new ResourceNotFoundException("Activity not found with that ID " + activity_id);
      }
    }
}
