package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.ActivityDTOAssembler;


import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
    private final PagedResourcesAssembler<Activity> pagedResourcesAssembler;
    private final ActivityDTOAssembler activityDTOAssembler;


    @Autowired
    public ActivityController(ActivityService activityService, PagedResourcesAssembler<Activity> pagedResourcesAssembler, ActivityDTOAssembler activityDTOAssembler) {
        this.activityService = activityService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.activityDTOAssembler = activityDTOAssembler;
    }

    /**
     * Get all scheduled activities in the database
     *
     * @return a page of Activities
     */
    @GetMapping("")
    @Operation(summary = "List all scheduled activities",
            description = "Get List of all scheduled activities and links to view the activitys details")
    public PagedModel<ActivityDTO> getActivities(Pageable pageable) {
        Page<Activity> allActivities = activityService.getActivities(pageable);
        return pagedResourcesAssembler.toModel(allActivities, activityDTOAssembler);
    }

    //get single activity
    @GetMapping("/{activity_id}")
    @Operation(summary = "Get specific scheduled activity",
            description = "Get a specific scheduled activity, and links to relevant operations")
    public ActivityDTO getActivityByActivityId(@Parameter(description = "The ID of the specific activity", required = true)@PathVariable Long activity_id) {
        ActivityDTO activity = activityDTOAssembler.toModel(activityService.findActivityById(activity_id));
        activity.add(linkTo(ActivityController.class).slash(activity_id).withRel("delete"));
        activity.add(linkTo(ActivityController.class).slash(activity_id).withRel("update"));
        activity.add(linkTo(BookingController.class).slash("activity").slash(activity_id).withRel("Bookings"));
        activity.add(linkTo(BookingController.class).slash(activity_id).withRel("Place Booking"));
        if (activity.getRegularSessionId() != null) {
            activity.add(linkTo(ActivityController.class).slash("cancelregularsession").slash(activity.getRegularSessionId().getId()).withRel("Stop Regular Session"));
        }
        return activity;
    }
//
    //get scheduled activities for a given resource
    @GetMapping("/resource/{resource_id}")
    @Operation(summary = "Get scheduled activities for a given resource",
            description = "Get list of all scheduled activities for a particular facility#2")
    public PagedModel<ActivityDTO> getActivitiesByResourceId(Pageable pageable, @Parameter(description = "The ID of the specific resource(facility)", required = true)@PathVariable Long resource_id) {
        return pagedResourcesAssembler.toModel(activityService.findByResourceId(pageable, resource_id), activityDTOAssembler);
    }

    //schedule an activity
    //Pulls data from activity type, only start and end type is pulled from json via JsonCreator
    //schedule an activity. Create a one time activity or regular session
    @PostMapping("/activitytype/{activity_type_id}")
    @Transactional
    @Operation(summary = "create a new scheduled activity",
            description = "create a new scheduled activity, using activity type. Has the option to make regular session #12")
    @PreAuthorize("hasAuthority('SCOPE_create:activity')")
    public ActivityDTO createActivity(@Parameter(description = "An ActivityDTO object, providing details needed to create an activity", required = true) @Valid @RequestBody ActivityDTO activityDTO,
                                   @Parameter(description = "An ActivityDTO object, providing details needed to create an activity", required = true) @PathVariable Long activity_type_id) {
        Activity activity = new Activity();
        RegularSession regularSession = new RegularSession();
        activity.setStartTime(activityDTO.getStartTime());
        activity.setEndTime(activityDTO.getEndTime());
        activity.setSocial(activityDTO.isSocial());
        activity.setResource(activityDTO.getResource());
        if (activityDTO.isRegularSession()) {
            regularSession.setInterval(activityDTO.getInterval());
        } else {
            regularSession = null;
        }
        final Activity activity1 = activityService.createNewActivity(activity, activity_type_id, regularSession);
        return activityDTOAssembler.toModel(activity1);
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
    @PreAuthorize("hasAuthority('SCOPE_delete:activity')")
    public ResponseEntity<?> deleteActivity(@Parameter(description = "The ID of the specific activity", required = true)@PathVariable Long activity_id) {
        return activityService.deleteActivity(activity_id);
    }

    //delete regular session, therefore stop activities from repeating
    @DeleteMapping("/cancelregularsession/{regular_session_id}")
    @Operation(summary = "Delete regular session",
            description = "Removes a regular session (which allows a scheduled activity to repeat) #2")
    @PreAuthorize("hasAuthority('SCOPE_delete:activity')")
    public ResponseEntity<?> deleteRegularSession(@Parameter(description = "The ID of the specific regular session", required = true)@PathVariable Long regular_session_id) {
        return activityService.deleteRegularSession(regular_session_id);
    }

}
