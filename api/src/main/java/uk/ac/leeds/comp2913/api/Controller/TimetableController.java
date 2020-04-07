package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * localhost:8000/swagger-ui.html
 * TODO: @CHORE, move domain logic into a service
 * TODO: @CHORE, add HAL to all endpoints, with links to where the client can find
 * *          the associated resource, account and activity  for the booking
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 */
@RestController
@RequestMapping("/timetable")
public class TimetableController {

    private final ActivityService activityService;
    private final PagedResourcesAssembler pagedResourcesAssembler;


    @Autowired
    public TimetableController(ActivityService activityService, PagedResourcesAssembler pagedResourcesAssembler) {
        this.activityService = activityService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    //View timetable for all facilities
    @GetMapping("")
    @Operation(summary = "Get Timetable",
            description = "Get list of all activities for all facilities #1")
    public PagedModel<Activity> getTimetable(Pageable pageable) {
        Page<Activity> timetabledActivities = activityService.findAllWithResources(pageable);

        //Create unique links for each activity
        for (Activity activity : timetabledActivities) {
            Long activityId = activity.getId();
            Link activityLink = linkTo(ActivityController.class).slash(activityId).withRel("Activity");
            Link resourceLink = linkTo(ResourceController.class).slash(activity.getResource().getId()).withRel("Resource");
            Link resourceTimetable = linkTo(TimetableController.class).slash(activity.getResource().getId()).withRel("View Timetable For Resource");
            Link bookingLink = linkTo(BookingController.class).slash(activity.getId()).withRel("Place Booking For This Activity");
            activity.add(activityLink, resourceLink, bookingLink, resourceTimetable);
        }
        //Links that are for full page
        Link viewTimetable = linkTo(TimetableController.class).withSelfRel();
        Link createNewResource = linkTo(TimetableController.class).withRel("Add New Resource");
        Link createNewActivity = linkTo(ActivityController.class).withRel("Add New Activity");
        PagedModel<Activity> result = pagedResourcesAssembler.toModel(timetabledActivities);
        result.add(viewTimetable, createNewActivity, createNewResource);
        return result;
    }

    //view timetable by specified facility
   @GetMapping("{resource_id}")
   @Operation(summary = "Get Timetable for a facility",
           description = "Get list of all activities for a particular facilities #2")
   public PagedModel<Activity> getTimetableByResource(Pageable pageable, @Parameter(description = "The ID of the facility/resource", required = true)@PathVariable Long resource_id) {
       Page<Activity> timetabledActivitiesByResource = activityService.findByResourceId(pageable, resource_id);
       //unique links for each activity
       for (Activity activity : timetabledActivitiesByResource) {
           Long activityId = activity.getId();
           Link activityLink = linkTo(ActivityController.class).slash(activityId).withRel("Activity");
           Link resourceLink = linkTo(ResourceController.class).slash(activity.getResource().getId()).withRel("Resource");
           Link bookingLink = linkTo(BookingController.class).slash(activity.getId()).withRel("Place Booking For This Activity");
           activity.add(activityLink, resourceLink, bookingLink);
       }
       //Links for full page
       Link viewTimetable = linkTo(TimetableController.class).withRel("View timetable for all facilities");
       Link viewResourceTimetable = linkTo(TimetableController.class).slash(resource_id).withSelfRel();
       Link createNewResource = linkTo(TimetableController.class).withRel("Add New Facility");
       Link createNewActivity = linkTo(ActivityController.class).withRel("Add New Activity");
       Link createNewActivityType = linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withRel("Create new Activity Type for Resource");

       PagedModel<Activity> result = pagedResourcesAssembler.toModel(timetabledActivitiesByResource);
       result.add(viewTimetable, viewResourceTimetable, createNewActivity, createNewResource, createNewActivityType, viewResourceTimetable);
       return result;
   }
}

