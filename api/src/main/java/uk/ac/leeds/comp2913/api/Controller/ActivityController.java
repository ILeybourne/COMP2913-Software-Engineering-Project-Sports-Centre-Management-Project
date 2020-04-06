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

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "List all scheduled activities",
            description = "Get List of all scheduled activities and links to view the activitys details")
    public CollectionModel<Activity> getActivities() {
        List<Activity> allActivities = activityService.getActivities();
        for (Activity activity : allActivities) {
            Long activityId = activity.getId();
            Link selfLink = linkTo(ActivityController.class).slash(activityId).withSelfRel();
            activity.add(selfLink);
        }
        Link viewAllActivities = linkTo(ActivityController.class).withSelfRel();
        Link createActivity = linkTo(ActivityController.class).withSelfRel();
        CollectionModel<Activity> result = new CollectionModel<>(allActivities, viewAllActivities, createActivity);
        return result;
    }

    //get single activity
    @GetMapping("/{activity_id}")
    @Operation(summary = "Get specific scheduled activity",
            description = "Get a specific scheduled activity, and links to relevant operations")
    public CollectionModel<Activity> getActivitiesByActivityId(@PathVariable Long activity_id) {
        Activity activity = activityService.findActivityById(activity_id);
        Link deleteLink = linkTo(ActivityController.class).slash(activity_id).slash("delete").withRel("delete");
        Link updateLink = linkTo(ActivityController.class).slash(activity_id).slash("update").withRel("update");
        Link viewBookingsLink = linkTo(BookingController.class).slash("activity").slash(activity_id).withRel("Bookings");
        Link placeBookingLink = linkTo(BookingController.class).slash(activity_id).withRel("Place Booking");
        Link createNewActivityLink = linkTo(ActivityController.class).withRel("Create New Activity");
        activity.add(updateLink, deleteLink, viewBookingsLink, placeBookingLink, createNewActivityLink);
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
    @Operation(summary = "Get scheduled activities for a given resource",
            description = "Get list of all scheduled activities for a particular facility#2")
    public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
        return activityService.findByResourceId(resource_id);
    }

    //schedule an activity
    //Pulls data from activity type, only start and end type is pulled from json via JsonCreator
    //schedule an activity. Create a one time activity or regular session
    @PostMapping("")
    @Operation(summary = "create a new scheduled activity",
            description = "create a new scheduled activity, using activity type. Has the option to make regular session #12")
    public Activity createActivity(@Valid @RequestBody ActivityDTO activityDTO) {
        Activity activity = new Activity();
        RegularSession regularSession = new RegularSession();
        Long activity_type_id = activityDTO.getActivityTypeId();
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
    @Operation(summary = "Update a scheduled activity",
            description = "Update the details of a scheduled activity #2")
    public Activity updateActivity(@PathVariable Long activity_id, @Valid @RequestBody Activity activityRequest) {
        return activityService.editActivity(activity_id, activityRequest);
    }

    //delete scheduled activity
    @DeleteMapping("/{activity_id}/delete")
    @Operation(summary = "Delete a scheduled activity",
            description = "delete a specific activity from the timetable/database #2")
    public ResponseEntity<?> deleteActivity(@PathVariable Long activity_id) {
        return activityService.deleteActivity(activity_id);
    }

    //delete regular session, therefore stop activities from repeating
    @DeleteMapping("/cancelregularsession/{regular_session_id}")
    @Operation(summary = "Delete regular session",
            description = "Removes a regular session (which allows a scheduled activity to repeat) #2")
    public ResponseEntity<?> deleteRegularSession(@PathVariable Long regular_session_id) {
        return activityService.deleteRegularSession(regular_session_id);
    }
}
