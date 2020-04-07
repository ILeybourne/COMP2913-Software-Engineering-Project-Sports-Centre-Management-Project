package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * localhost:8000/swagger-ui.html
 * TODO: @CHORE, move domain logic into a service @DEPENDENCY for Testing
 * TODO: @CHORE, add HAL to all endpoints
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 * https://www.baeldung.com/spring-hateoas-tutorial?fbclid=IwAR2NW80nLYxRXVDCwtIvCa2-ntc6CzfK54qjf8KALi6_CWlX5jcz9YghPQo
 *
 * TODO: PAGINATION CONFIG FOR SORTING
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final PagedResourcesAssembler pagedResourcesAssembler;


    @Autowired
    public ActivityController(ActivityService activityService, PagedResourcesAssembler pagedResourcesAssembler) {
        this.activityService = activityService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * Get all scheduled activities in the database
     *
     * @return a page of Activities
     */
    @GetMapping("")
    @Operation(summary = "List all scheduled activities",
            description = "Get List of all scheduled activities and links to view the activitys details")
    public PagedModel<Activity> getActivities(Pageable pageable) {
        Page<Activity> allActivities = activityService.getActivities(pageable);
        for (Activity activity : allActivities) {
            Long activityId = activity.getId();
            Link selfLink = linkTo(ActivityController.class).slash(activityId).withSelfRel();
            activity.add(selfLink);
        }
        Link viewAllActivities = linkTo(ActivityController.class).withSelfRel();
        PagedModel<Activity> result = pagedResourcesAssembler.toModel(allActivities);
        result.add(viewAllActivities);
        return result;
    }

    //get single activity
    @GetMapping("/{activity_id}")
    @Operation(summary = "Get specific scheduled activity",
            description = "Get a specific scheduled activity, and links to relevant operations")
    public Activity getActivityByActivityId(@Parameter(description = "The ID of the specific activity", required = true)@PathVariable Long activity_id) {
        Activity activity = activityService.findActivityById(activity_id);
        Link deleteLink = linkTo(ActivityController.class).slash(activity_id).withRel("delete");
        Link updateLink = linkTo(ActivityController.class).slash(activity_id).withRel("update");
        Link viewBookingsLink = linkTo(BookingController.class).slash("activity").slash(activity_id).withRel("Bookings");
        Link placeBookingLink = linkTo(BookingController.class).slash(activity_id).withRel("Place Booking");
        activity.add(updateLink, deleteLink, viewBookingsLink, placeBookingLink);
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
    public PagedModel<Activity> getActivitiesByResourceId(Pageable pageable, @Parameter(description = "The ID of the specific resource(facility)", required = true)@PathVariable Long resource_id) {
        Page<Activity> activitiesByResource = activityService.findByResourceId(pageable, resource_id);
        PagedModel<Activity> result = pagedResourcesAssembler.toModel(activitiesByResource);
        return result;
    }

    //schedule an activity
    //Pulls data from activity type, only start and end type is pulled from json via JsonCreator
    //schedule an activity. Create a one time activity or regular session
    @PostMapping("activitytype/{activity_type_id}")
    @Operation(summary = "create a new scheduled activity",
            description = "create a new scheduled activity, using activity type. Has the option to make regular session #12")
    public Activity createActivity(@Parameter(description = "An ActivityDTO object, providing details needed to create an activity", required = true) @Valid @RequestBody ActivityDTO activityDTO,
                                   @Parameter(description = "An ActivityDTO object, providing details needed to create an activity", required = true) @PathVariable Long activity_type_id) {
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
    @PutMapping("/{activity_id}")
    @Operation(summary = "Update a scheduled activity",
            description = "Update the details of a scheduled activity #2")
    public Activity updateActivity(@Parameter(description = "The ID of the specific activity", required = true)@PathVariable Long activity_id, @Valid @RequestBody Activity activityRequest) {
        return activityService.editActivity(activity_id, activityRequest);
    }

    //delete scheduled activity
    @DeleteMapping("/{activity_id}")
    @Operation(summary = "Delete a scheduled activity",
            description = "delete a specific activity from the timetable/database #2")
    public ResponseEntity<?> deleteActivity(@Parameter(description = "The ID of the specific activity", required = true)@PathVariable Long activity_id) {
        return activityService.deleteActivity(activity_id);
    }

    //delete regular session, therefore stop activities from repeating
    @DeleteMapping("/cancelregularsession/{regular_session_id}")
    @Operation(summary = "Delete regular session",
            description = "Removes a regular session (which allows a scheduled activity to repeat) #2")
    public ResponseEntity<?> deleteRegularSession(@Parameter(description = "The ID of the specific regular session", required = true)@PathVariable Long regular_session_id) {
        return activityService.deleteRegularSession(regular_session_id);
    }

}
