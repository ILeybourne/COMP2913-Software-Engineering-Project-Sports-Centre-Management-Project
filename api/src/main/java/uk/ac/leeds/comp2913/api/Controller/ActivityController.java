package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service @DEPENDENCY for Testing
 * TODO: @CHORE, add HAL to all endpoints
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 * https://www.baeldung.com/spring-hateoas-tutorial?fbclid=IwAR2NW80nLYxRXVDCwtIvCa2-ntc6CzfK54qjf8KALi6_CWlX5jcz9YghPQo
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Get all scheduled activities in the database
     *
     * @return a page of Activities
     */
    @GetMapping
    public CollectionModel<Activity> getActivities() {
        List<Activity> allActivities = activityService.getActivities();
        for (Activity activity : allActivities) {
            Long activityId = activity.getId();
            Link selfLink = linkTo(ActivityController.class).slash(activityId).withSelfRel();
            Link updateLink = linkTo(ActivityController.class).slash(activityId).slash("update").withRel("update");
            Link deleteLink = linkTo(ActivityController.class).slash(activityId).slash("delete").withRel("delete");
            activity.add(selfLink, updateLink, deleteLink);
            if (activity.getRegularSession() != null) {
                Long regularSessionId = activity.getRegularSession().getId();
                Link stopRegularSessionLink = linkTo(ActivityController.class).slash("cancelregularsession")
                        .slash(regularSessionId).withRel("Stop Regular Session");
                activity.add(stopRegularSessionLink);
            }
        }
        Link viewAllActivities = linkTo(ActivityController.class).withSelfRel();
        //  Link createActivity = linkTo(ActivityController.class).withRel("create new activity");
        CollectionModel<Activity> result = new CollectionModel<>(allActivities, viewAllActivities);
        return result;
    }

    //get single activity
    @GetMapping("/{activity_id}")
    public CollectionModel<Activity> getActivitiesByActivityId(@PathVariable Long activity_id) {
        Activity activity = activityService.findActivityById(activity_id);
        Link deleteLink = linkTo(ActivityController.class).slash(activity_id).slash("delete").withRel("delete");
        Link updateLink = linkTo(ActivityController.class).slash(activity_id).slash("update").withRel("update");
        activity.add(updateLink, deleteLink);
        if (activity.getRegularSession() != null) {
            Long regularSessionId = activity.getRegularSession().getId();
            Link stopRegularSessionLink = linkTo(ActivityController.class).slash("cancelregularsession")
                    .slash(regularSessionId).withRel("Stop Regular Session");
            activity.add(stopRegularSessionLink);
        }
        return activity;
    }

    //get scheduled activities for a given resource
    @GetMapping("/resource/{resource_id}")
    public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
        return activityService.findByResourceId(resource_id);
    }

    //schedule an activity
    //Pulls data from activity type, only start and end type is pulled from json via JsonCreator
    //schedule an activity. Create a one time activity or regular session
    @PostMapping("{activity_type_id}")
    public Activity createActivity(@Valid @RequestBody ActivityDTO activityDTO, @PathVariable Long activity_type_id) {
        Activity activity = new Activity();
        RegularSession regularSession = new RegularSession();
        activity.setStartTime(activityDTO.getStartTime());
        activity.setEndTime(activityDTO.getEndTime());
        activity.setSocial(activityDTO.isSocial());
        if (activityDTO.isRegularSession()) {
            regularSession.setInterval(activityDTO.getInterval());
        } else {
            regularSession = null;
        }
        return activityService.createNewActivity(activity, activity_type_id, regularSession);
    }

    //update details of scheduled activity
    @PutMapping("/{activity_id}/update")
    public Activity updateActivity(@PathVariable Long activity_id, @Valid @RequestBody Activity activityRequest) {
        return activityService.editActivity(activity_id, activityRequest);
    }

    //delete scheduled activity
    @DeleteMapping("/{activity_id}/delete")
    public ResponseEntity<?> deleteActivity(@PathVariable Long activity_id) {
        return activityService.deleteActivity(activity_id);
    }

    //delete regular session, therefore stop activities from repeating
    @DeleteMapping("/cancelregularsession/{regular_session_id}")
    public ResponseEntity<?> deleteRegularSession(@PathVariable Long regular_session_id) {
        return activityService.deleteRegularSession(regular_session_id);
    }
}
