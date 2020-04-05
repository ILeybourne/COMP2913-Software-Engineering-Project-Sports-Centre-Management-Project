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
    public CollectionModel<ActivityType> getActivityTypes() {
        List<ActivityType> allActivityTypes =  activityTypeService.findAll();
        for (ActivityType activityType : allActivityTypes) {
            Long activityTypeId = activityType.getId();
            Link selfLink = linkTo(ActivityTypeController.class).slash(activityTypeId).withSelfRel();
            Link updateLink = linkTo(ActivityTypeController.class).slash(activityTypeId).slash("update").withRel("update");
            Link deleteLink = linkTo(ActivityTypeController.class).slash(activityTypeId).slash("delete").withRel("delete");
            Link createNewActivity = linkTo(ActivityController.class).slash(activityTypeId).withRel("Create New Scheduled Activity");
            activityType.add(selfLink, updateLink, deleteLink, createNewActivity);
        }
        Link viewAllActivityTypes = linkTo(ActivityTypeController.class).withSelfRel();
        CollectionModel<ActivityType> result = new CollectionModel<>(allActivityTypes, viewAllActivityTypes);
        return result;
    }

    //Get activity types for a resource
    @GetMapping("/resource/{resource_id}")
    public List<ActivityType> getActivityTypesByResourceId(@PathVariable Long resource_id) {
        return activityTypeService.findByResourceId(resource_id);
    }

    //add new activity type to resource
    @PostMapping("/resource/{resource_id}")
    public ActivityType addActivityType(@PathVariable Long resource_id, @Valid @RequestBody ActivityType activityType) {
        return activityTypeService.addActivityType(resource_id, activityType);
    }

    //update activity type
    @PutMapping("/{activity_type_id}/update")
    public ActivityType updateActivityType(@PathVariable Long activity_type_id, @Valid @RequestBody ActivityType activityTypeRequest) {
        return activityTypeService.updateActivityType(activity_type_id, activityTypeRequest);
    }

    //delete activity
    @DeleteMapping("/{activity_type_id}/delete")
    public ResponseEntity<?> deleteActivityType(@PathVariable Long activity_type_id) {
        return activityTypeService.deleteActivityType(activity_type_id);
    }
}
