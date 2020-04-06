package uk.ac.leeds.comp2913.api.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service
 * TODO: @CHORE, add HAL to all endpoints, with links to where the client can find
 * *          the associated resource, account and activity  for the booking
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 * <p>
 * TODO: @FEATURE, file upload for image of facility
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {

    Logger logger = LoggerFactory.getLogger(ResourceController.class);

    private final ResourceService resourceService;
    private final ActivityTypeService activityTypeService;

    public ResourceController(ResourceService resourceService, ActivityTypeService activityTypeService) {
        this.resourceService = resourceService;
        this.activityTypeService = activityTypeService;
    }

    //Get Resources
    @GetMapping("")
    @Operation(summary = "Get all facilities",
            description = "Get a list of all facilities with basic information")
//    @PreAuthorize("hasAuthority('SCOPE_read:resource')")
    public CollectionModel<Resource> getResources() {
        List<Resource> allResources = resourceService.findAll();
        for(Resource resource : allResources) {
            Long resourceId = resource.getId();
            Link selfLink = linkTo(ResourceController.class).slash(resourceId).withSelfRel();
            resource.add(selfLink);
        }
        Link createResource = linkTo(ResourceController.class).withRel("Create new resource");
        Link viewAllResources = linkTo(ResourceController.class).withSelfRel();
        CollectionModel<Resource> result = new CollectionModel<>(allResources);
        result.add(viewAllResources, createResource);
        return result;
    }

    @GetMapping("/{resource_id}")
    @Operation(summary = "Get a specific facility",
            description = "Get a specific facility with more information and links")
  //  @PreAuthorize("hasAuthority('SCOPE_read:resource')")
    public Resource indexResource(@PathVariable Long resource_id) {
        Resource resource = resourceService.findById(resource_id);
        Link selfLink = linkTo(ResourceController.class).slash(resource_id).withSelfRel();
        Link updateLink = linkTo(ResourceController.class).slash(resource_id).withRel("update");
        Link deleteLink = linkTo(ResourceController.class).slash(resource_id).withRel("delete");
        Link viewActivities = linkTo(ActivityController.class).slash("resource").slash(resource_id).withRel("View Resources Scheduled Activities");
        Link viewActivityTypes = linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withRel("View Activity Types for Resource");
        Link newActivityTypeLink = linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withRel("Create new Activity Type for Resource");
        resource.add(selfLink, updateLink, deleteLink, viewActivities, viewActivityTypes, newActivityTypeLink);
        return resource;
    }

    //Post new resource
    @PostMapping("")
    @Operation(summary = "Create a new facility",
            description = "Create a new facility")
    @PreAuthorize("hasAuthority('SCOPE_create:resource')")
    public Resource createResource(@Valid @RequestBody Resource resource) {
        return resourceService.create(resource);
    }

    //update resource
    @PutMapping("/{resource_id}")
    @Operation(summary = "Update a facility",
            description = "Update the details of a facility")
    @PreAuthorize("hasAuthority('SCOPE_update:resource')")
    public Resource updateResource(@PathVariable Long resource_id, @Valid @RequestBody Resource resourceRequest) {
        return resourceService.update(resource_id, resourceRequest);
    }

    //delete resource
    @DeleteMapping("/{resource_id}")
    @Operation(summary = "delete a facility",
            description = "Delete a facility")
    @PreAuthorize("hasAuthority('SCOPE_delete:resource')")
    public ResponseEntity<?> deleteResource(@PathVariable Long resource_id) {
        resourceService.deleteById(resource_id);
        return ResponseEntity.ok()
                .build();
    }
}

