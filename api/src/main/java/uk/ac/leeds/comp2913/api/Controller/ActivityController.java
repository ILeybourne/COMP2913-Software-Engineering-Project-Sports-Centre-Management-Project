package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

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
    private final ActivityService activityService;
    private final RegularSessionRepository regularSessionRepository;
    private final ActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityController(ActivityRepository activityRepository, ActivityTypeRepository activityTypeRepository, ResourceRepository resourceRepository) {
        this.activityRepository = activityRepository;
        this.activityService = activityService;
        this.regularSessionRepository = regularSessionRepository;
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
    //schedule an activity. Create a one time activity or regular session
    @PostMapping("activitytype/{activity_type_id}")
    public Activity createActivity(@Valid @RequestBody ActivityDTO activity, @PathVariable Long activity_type_id) {
        ActivityType activityType = activityTypeRepository.findById(activity_type_id)
                .orElseThrow(() -> new ResourceNotFoundException("The Activity Type " + resource_id + " could not be found"));
        Activity a = new Activity();
        RegularSession regularSession = new RegularSession();
        a.setStartTime(activity.getStartTime());
        a.setEndTime(activity.getEndTime());
        a.setSocial(activity.isSocial());
        if (activity.isRegularSession()) {
            regularSession.setInterval(activity.getInterval());
        } else {
            regularSession = null;
        }
        return activityService.createNewActivity(a, activity_type_id, regularSession);
    }
    return activityService.createNewActivity(a,activity_type_id,regularSession);
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
        activity.setRegularSession(activityRequest.getRegularSession());

        return activityRepository.save(activity);
    }

    //delete scheduled activity
    @DeleteMapping("/{activity_id}")
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
