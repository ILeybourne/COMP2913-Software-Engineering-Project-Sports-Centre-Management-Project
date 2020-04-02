package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import javax.validation.Valid;

import java.util.List;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service @DEPENDENCY for Testing
 * TODO: @CHORE, add HAL to all endpoints
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final ActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityController(ActivityRepository activityRepository, ActivityTypeRepository activityTypeRepository, ResourceRepository resourceRepository) {
        this.activityRepository = activityRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    /**
     * Get all scheduled activities in the database
     *
     * @param pageable Pagination Metadata
     * @return a page of Activities
     */
    @GetMapping("")
    public Page<Activity> getActivities(Pageable pageable) {
        return activityRepository.findAll(pageable);
    }

    //get scheduled activities for a given resource
    @GetMapping("/resource/{resource_id}")
    public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
        return activityRepository.findByResourceId(resource_id);
    }

    //schedule an activity
    //Pulls data from activity type, only start and end type is pulled from json via JsonCreator
    @PostMapping("activitytype/{activity_type_id}")
    public Activity createActivity(@PathVariable Long resource_id, @PathVariable Long activity_type_id, @Valid @RequestBody Activity activity) {
        ActivityType activityType = activityTypeRepository.findById(activity_type_id)
                .orElseThrow(() -> new ResourceNotFoundException("The Activity Type " + resource_id + " could not be found"));

        activity.setCost(activityType.getCost());
        activity.setCurrentCapacity(activityType.getTotalCapacity());
        activity.setName(activityType.getName());
        activity.setResource(activityType.getResource());

        return activityRepository.save(activity);
    }

    //update details of scheduled activity
    @PutMapping("/{activity_id}")
    public Activity updateActivity(@PathVariable Long activity_id, @Valid @RequestBody Activity activityRequest) {

        Activity activity = activityRepository.findById(activity_id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID " + activity_id));

        activity.setName(activityRequest.getName());
        activity.setCost(activityRequest.getCost());
        activity.setStartTime(activityRequest.getStartTime());
        activity.setEndTime(activityRequest.getEndTime());
        activity.setCurrentCapacity(activityRequest.getCurrentCapacity());

        return activityRepository.save(activity);
    }

    //delete scheduled activity
    @DeleteMapping("/{activity_id}")
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
