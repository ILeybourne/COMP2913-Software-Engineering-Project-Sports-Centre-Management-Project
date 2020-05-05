package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityTypeDTO;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.ActivityTypePagedResourcesAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * localhost:8000/swagger-ui.html
 * TODO: @CHORE, move domain logic into a service @DEPENDENCY for Testing
 * TODO: @CHORE, add HAL to all endpoints
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 */
@RestController
@RequestMapping("/activitytypes")
public class ActivityTypeController {

    private final ActivityTypeService activityTypeService;
    private final PagedResourcesAssembler<ActivityType> pagedResourcesAssembler;
    private final ActivityTypePagedResourcesAssembler activityTypePagedResourcesAssembler;


    @Autowired
    public ActivityTypeController(ActivityTypeService activityTypeService, PagedResourcesAssembler<ActivityType> pagedResourcesAssembler, ActivityTypePagedResourcesAssembler activityTypePagedResourcesAssembler) {
        this.activityTypeService = activityTypeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.activityTypePagedResourcesAssembler = activityTypePagedResourcesAssembler;
    }

    /**
     * Get all activity details required to schedule an activity
     *
     */

    @GetMapping("")
    @Operation(summary = "Get all activity types",
            description = "Get list all activity types (used as templates for scheduling activities) #2")
    public PagedModel<ActivityTypeDTO> getActivityTypes(Pageable pageable) {
        Page<ActivityType> allActivityTypes =  activityTypeService.findAll(pageable);
        return pagedResourcesAssembler.toModel(allActivityTypes, activityTypePagedResourcesAssembler);
    }

    //Get activity types for a resource
    @GetMapping("/{activity_type_id}")
    @Operation(summary = "Get a specific activity type",
            description = "Get a specific activity type with links to its details/relevant operations #2")
    public ActivityTypeDTO getActivityTypeId(@Parameter(description = "The ID of the activity type", required = true)@PathVariable Long activity_type_id) {
        ActivityTypeDTO activityType = activityTypePagedResourcesAssembler.toModel(activityTypeService.findById(activity_type_id));
        activityType.add(linkTo(ActivityTypeController.class).slash(activity_type_id).withRel("update"));
        activityType.add(linkTo(ActivityTypeController.class).slash(activity_type_id).withRel("delete"));
        activityType.add(linkTo(ActivityController.class).slash("activitytype").slash(activity_type_id).withRel("Create New Scheduled Activity from this template"));
        return activityType;
    }

    //Get activity types for a resource
    @GetMapping("/resource/{resource_id}")
    @Operation(summary = "Get a list of activity types for a facility",
            description = "Get list of all activity types for a particular facilities" +
                    "used for scheduling activities #2")
    public PagedModel<ActivityTypeDTO> getActivityTypesByResourceId(Pageable pageable, @Parameter(description = "The ID of the resource", required = true)@PathVariable Long resource_id) {
        Page<ActivityType> allActivityTypes =  activityTypeService.findByResourceId(pageable, resource_id);
        PagedModel<ActivityTypeDTO> result = pagedResourcesAssembler.toModel(allActivityTypes, activityTypePagedResourcesAssembler);
        return result;
    }

    //add new activity type to resource
    @PostMapping("/resource/{resource_id}")
    @Operation(summary = "Create a new activity type that occurs for a resource",
            description = "create a new acitivity type for a particular resource #2")
    public ActivityTypeDTO addActivityType(@Parameter(description = "The ID of the resource", required = true) @PathVariable Long resource_id,
                                        @Parameter(description = "An activity type object", required = true) @Valid @RequestBody ActivityType activityType) {
        return activityTypePagedResourcesAssembler.toModel(activityTypeService.addActivityType(resource_id, activityType));
    }

    //update activity type
    @PutMapping("/{activity_type_id}")
    @Operation(summary = "Update activity type",
            description = "edit the details of an activity type #2")
    public ActivityTypeDTO updateActivityType(@Parameter(description = "The ID of the activity type", required = true)@PathVariable Long activity_type_id,
                                           @Parameter(description = "An activity type object", required = true)@Valid @RequestBody ActivityType activityTypeRequest) {
        ActivityType activityType = activityTypeService.updateActivityType(activity_type_id, activityTypeRequest);
        return activityTypePagedResourcesAssembler.toModel(activityType);
    }

    //delete activity
    @DeleteMapping("/{activity_type_id}")
    @Operation(summary = "delete an activity type",
            description = "delete an activity type #2")
    public ResponseEntity<?> deleteActivityType(@Parameter(description = "The id of the activity type", required = true)@PathVariable Long activity_type_id) {
        return activityTypeService.deleteActivityType(activity_type_id);
    }
}
