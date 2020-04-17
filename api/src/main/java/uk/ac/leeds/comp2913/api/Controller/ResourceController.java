package uk.ac.leeds.comp2913.api.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.ResourcePagedResourcesAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.ResourceDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * localhost:8000/swagger-ui.html
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
    private final PagedResourcesAssembler<Resource> pagedResourcesAssembler;
    private final ResourcePagedResourcesAssembler resourcePagedResourcesAssembler;


    public ResourceController(ResourceService resourceService, ActivityTypeService activityTypeService, PagedResourcesAssembler<Resource> pagedResourcesAssembler, ResourcePagedResourcesAssembler resourcePagedResourcesAssembler) {
        this.resourceService = resourceService;
        this.activityTypeService = activityTypeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.resourcePagedResourcesAssembler = resourcePagedResourcesAssembler;
    }

    //Get Resources
    @GetMapping("")
    @Operation(summary = "Get all facilities",
            description = "Get a list of all facilities with basic information")
//    @PreAuthorize("hasAuthority('SCOPE_read:resources')")
    public PagedModel<ResourceDTO> getResources(Pageable pageable) {
        Page<Resource> allResources = resourceService.findAll(pageable);
        return pagedResourcesAssembler.toModel(allResources, resourcePagedResourcesAssembler);
    }

    @GetMapping("/{resource_id}")
    @Operation(summary = "Get a specific facility",
            description = "Get a specific facility with more information and links")
//    @PreAuthorize("hasAuthority('SCOPE_read:resources')")
    public ResourceDTO getResourceById(@Parameter(description = "The ID of the facility/resource", required = true)@PathVariable Long resource_id) {
        ResourceDTO resource = resourcePagedResourcesAssembler.toModel(resourceService.findById(resource_id));
        resource.add(linkTo(ResourceController.class).slash(resource_id).withRel("update"));
        resource.add(linkTo(ResourceController.class).slash(resource_id).withRel("delete"));
        resource.add(linkTo(ActivityTypeController.class).slash("resource").slash(resource_id).withRel("Create new Activity Type for Resource"));
        resource.add(linkTo(ResourceController.class).withRel("Create new resource"));
        return resource;
    }

    //Post new resource
    @PostMapping("")
    @Operation(summary = "Create a new facility",
            description = "Create a new facility")
//    @PreAuthorize("hasAuthority('SCOPE_create:resources')")
    public Resource createResource(@Parameter(description = "The ID of the facility/resource", required = true)@Valid @RequestBody Resource resource) {
        return resourceService.create(resource);
    }

    //update resource
    @PutMapping("/{resource_id}")
    @Operation(summary = "Update a facility",
            description = "Update the details of a facility")
//    @PreAuthorize("hasAuthority('SCOPE_update:resources')")
    public Resource updateResource(@Parameter(description = "The ID of the facility/resource", required = true)@PathVariable Long resource_id,
                                   @Parameter(description = "A Resource object", required = true)@Valid @RequestBody Resource resourceRequest) {
        return resourceService.update(resource_id, resourceRequest);
    }

    //delete resource
    @DeleteMapping("/{resource_id}")
    @Operation(summary = "delete a facility",
            description = "Delete a facility")
//    @PreAuthorize("hasAuthority('SCOPE_delete:resource')")
    public ResponseEntity<?> deleteResource( @Parameter(description = "The ID of the facility/resource", required = true)@PathVariable Long resource_id) {
        resourceService.deleteById(resource_id);
        return ResponseEntity.ok()
                .build();
    }
}

