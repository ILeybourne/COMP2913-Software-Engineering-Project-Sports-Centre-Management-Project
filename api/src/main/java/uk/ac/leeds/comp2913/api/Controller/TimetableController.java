package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service
 * TODO: @CHORE, add HAL to all endpoints, with links to where the client can find
 * *          the associated resource, account and activity  for the booking
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 */
@RestController
@RequestMapping("/timetable")
public class TimetableController {

    private final ActivityService activityService;

    @Autowired
    public TimetableController(ActivityService activityService) {
        this.activityService = activityService;
    }

    //View timetable for all facilities
    @GetMapping("")
    @Operation(summary = "Get Timetable",
            description = "Get list of all activities for all facilities #1")
    public CollectionModel<Activity> getTimetable() {
        Collection<Activity> timetabledActivities = activityService.findAllWithResources();

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
        CollectionModel<Activity> result = new CollectionModel<>(timetabledActivities, viewTimetable, createNewActivity, createNewResource);
        return result;
    }

    //view timetable by specified facility
   @GetMapping("{resource_id}")
   @Operation(summary = "Get Timetable for a facility",
           description = "Get list of all activities for a particular facilities #2")
   public CollectionModel<Activity> getTimetableByResource(@PathVariable Long resource_id) {
       Collection<Activity> timetabledActivitiesByResource = activityService.findByResourceId(resource_id);
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

       CollectionModel<Activity> result = new CollectionModel<>(timetabledActivitiesByResource, viewTimetable, createNewActivity, createNewResource, createNewActivityType, viewResourceTimetable);
       return result;
   }
}

