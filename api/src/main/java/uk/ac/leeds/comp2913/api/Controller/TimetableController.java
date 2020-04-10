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
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.ActivityPagedResourcesAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.ActivityTypePagedResourcesAssembler;

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
    private final PagedResourcesAssembler<Activity> pagedResourcesAssembler;
    private final ActivityPagedResourcesAssembler activityPagedResourcesAssembler;


    @Autowired
    public TimetableController(ActivityService activityService, PagedResourcesAssembler<Activity> pagedResourcesAssembler, ActivityPagedResourcesAssembler activityPagedResourcesAssembler) {
        this.activityService = activityService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.activityPagedResourcesAssembler = activityPagedResourcesAssembler;
    }

    //View timetable for all facilities
  @GetMapping("")
  @Operation(summary = "Get Timetable",
          description = "Get list of all activities for all facilities #1")
  public PagedModel<ActivityDTO> getTimetable(Pageable pageable) {
      PagedModel<ActivityDTO> timetableForAllFacilities = pagedResourcesAssembler.toModel((activityService.findAllWithResources(pageable)), activityPagedResourcesAssembler);
      //Create unique links for each activity
      for (ActivityDTO activity : timetableForAllFacilities) {
          activity.add(linkTo(ResourceController.class).slash(activity.getResource().getId()).withRel("Resource"));
          activity.add(linkTo(TimetableController.class).slash(activity.getResource().getId()).withRel("View Facility Timetable"));
          activity.add(linkTo(BookingController.class).slash(activity.getId()).withRel("Place Booking For This Activity"));
      }
      timetableForAllFacilities.add(linkTo(ResourceController.class).withRel("Add New Facility"));

      return timetableForAllFacilities;
  }

  //view timetable by specified facility
 @GetMapping("{resource_id}")
 @Operation(summary = "Get Timetable for a facility",
         description = "Get list of all activities for a particular facilities #2")
 public PagedModel<ActivityDTO> getTimetableByResource(Pageable pageable, @Parameter(description = "The ID of the facility/resource", required = true)@PathVariable Long resource_id) {
     PagedModel<ActivityDTO> facilityTimetable = pagedResourcesAssembler.toModel((activityService.findByResourceId(pageable, resource_id)), activityPagedResourcesAssembler);

         //unique links for each activity
     for (ActivityDTO activity : facilityTimetable) {
         activity.add(linkTo(ActivityController.class).slash(activity.getId()).withRel("Activity"));
         activity.add(linkTo(ResourceController.class).slash(activity.getResource().getId()).withRel("Resource"));
         activity.add(linkTo(BookingController.class).slash(activity.getId()).withRel("Place Booking For This Activity"));
     }
     //Links for full page
       facilityTimetable.add(linkTo(TimetableController.class).withRel("View timetable for all facilities"));
       facilityTimetable.add(linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withRel("View Activity Types for this resource"));
       facilityTimetable.add(linkTo(ResourceController.class).withRel("Add New Facility"));
       facilityTimetable.add(linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withRel("Create new Activity Type for Resource"));
       return facilityTimetable;
 }
}

