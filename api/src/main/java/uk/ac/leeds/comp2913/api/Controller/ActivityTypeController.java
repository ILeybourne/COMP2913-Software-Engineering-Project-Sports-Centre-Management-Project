package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service @DEPENDENCY for Testing
 * TODO: @CHORE, add HAL to all endpoints
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 */
@RestController
@RequestMapping("/activitytypes")
public class ActivityTypeController {

    private final ActivityTypeService activityTypeService;

    @Autowired
    public ActivityTypeController(ActivityTypeService activityTypeService) {
        this.activityTypeService = activityTypeService;
    }

    /**
     * Get all activity details required to schedule an activity
     *
     */

    @GetMapping("")
    @Operation(summary = "Get all activity types",
            description = "Get list all activity types (used as templates for scheduling activities) #2")
    public CollectionModel<ActivityType> getActivityTypes() {
        List<ActivityType> allActivityTypes =  activityTypeService.findAll();
        for (ActivityType activityType : allActivityTypes) {
            Long activityTypeId = activityType.getId();
            Link selfLink = linkTo(ActivityTypeController.class).slash(activityTypeId).withSelfRel();
            activityType.add(selfLink);
        }
        Link viewAllActivityTypes = linkTo(ActivityTypeController.class).withSelfRel();
        CollectionModel<ActivityType> result = new CollectionModel<>(allActivityTypes, viewAllActivityTypes);
        return result;
    }

    //Get activity types for a resource
    @GetMapping("/{activity_type_id}")
    @Operation(summary = "Get a specific activity type",
            description = "Get a specific activity type with links to its details/relevant operations #2")
    public ActivityType getActivityTypeId(@PathVariable Long activity_type_id) {
        ActivityType activityType = activityTypeService.findById(activity_type_id);
        Link selfLink = linkTo(ActivityTypeController.class).slash(activity_type_id).withSelfRel();
        Link updateLink = linkTo(ActivityTypeController.class).slash(activity_type_id).slash("update").withRel("update");
        Link deleteLink = linkTo(ActivityTypeController.class).slash(activity_type_id).slash("delete").withRel("delete");
        Link resourceLink = linkTo(ResourceController.class).slash(activityType.getResource().getId()).withRel("Resource");
        Link createNewActivity = linkTo(ActivityController.class).slash(activity_type_id).withRel("Create New Scheduled Activity from this template");
        activityType.add(selfLink, updateLink, deleteLink, resourceLink, createNewActivity);
        return activityType;
    }

    //Get activity types for a resource
    @GetMapping("/resource/{resource_id}")
    @Operation(summary = "Get a list of activity types for a facility",
            description = "Get list of all activity types for a particular facilities" +
                    "used for scheduling activities #2")
    public CollectionModel<ActivityType> getActivityTypesByResourceId(@PathVariable Long resource_id) {
        List<ActivityType> allActivityTypes =  activityTypeService.findByResourceId(resource_id);
        for (ActivityType activityType : allActivityTypes) {
            Long activityTypeId = activityType.getId();
            Link selfLink = linkTo(ActivityTypeController.class).slash(activityTypeId).withSelfRel();
            activityType.add(selfLink);
        }
        Link viewAllActivityTypes = linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withSelfRel();
        Link resourceLink = linkTo(ResourceController.class).slash(resource_id).withRel("resource");
        CollectionModel<ActivityType> result = new CollectionModel<>(allActivityTypes, viewAllActivityTypes, resourceLink);
        return result;
    }

    //add new activity type to resource
    @PostMapping("/resource/{resource_id}")
    @Operation(summary = "Create a new activity type that occurs for a resource",
            description = "create a new acitivity type for a particular resource #2")
    public ActivityType addActivityType(@PathVariable Long resource_id, @Valid @RequestBody ActivityType activityType) {
        return activityTypeService.addActivityType(resource_id, activityType);
    }

    //update activity type
    @PutMapping("/{activity_type_id}/update")
    @Operation(summary = "Update activity type",
            description = "edit the details of an activity type #2")
    public ActivityType updateActivityType(@PathVariable Long activity_type_id, @Valid @RequestBody ActivityType activityTypeRequest) {
        return activityTypeService.updateActivityType(activity_type_id, activityTypeRequest);
    }

    //delete activity
    @DeleteMapping("/{activity_type_id}/delete")
    @Operation(summary = "delete an activity type",
            description = "delete an activity type #2")
    public ResponseEntity<?> deleteActivityType(@PathVariable Long activity_type_id) {
        return activityTypeService.deleteActivityType(activity_type_id);
    }
}
