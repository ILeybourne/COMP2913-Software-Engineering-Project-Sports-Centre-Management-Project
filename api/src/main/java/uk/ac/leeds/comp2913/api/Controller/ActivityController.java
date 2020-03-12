package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:8080")
public class ActivityController {

    private final ActivityRepository activityRepository;

    private final ResourceRepository resourceRepository;

    @Autowired
    public ActivityController(ActivityRepository activityRepository, ResourceRepository resourceRepository) {
      this.activityRepository = activityRepository;
      this.resourceRepository = resourceRepository;
    }


  /**
   * Get all activities in the database
   *
   * @param pageable
   * @return
   */
  @GetMapping("/activities")
  public Page<Activity> getActivities(Pageable pageable) {
    return activityRepository.findAll(pageable);
  }

  //get activities by resource ID
    @GetMapping("/resources/{resource_id}/activities")
    public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
        return activityRepository.findByResourceId(resource_id);
    }

    //add new activity to resource
    @PostMapping("/resources/{resource_id}/activities")
    public Activity AddActivity(@PathVariable Long resource_id, @Valid @RequestBody Activity activity) {
      return resourceRepository.findById(resource_id)
                .map(resource -> {
                  activity.setResource(resource);
                  return activityRepository.save(activity);
                }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
    }

    //update activity
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

    //delete activity
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
