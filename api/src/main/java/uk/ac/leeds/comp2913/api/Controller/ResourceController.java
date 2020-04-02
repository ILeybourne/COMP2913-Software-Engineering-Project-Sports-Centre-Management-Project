package uk.ac.leeds.comp2913.api.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;

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

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    //Get Resources
    @GetMapping("")
//    @PreAuthorize("hasAuthority('SCOPE_read:resource')")
    public Page<Resource> getResources(Pageable pageable) {
        return resourceService.findAll(pageable);
    }

    @GetMapping("/{resource_id}")
    @PreAuthorize("hasAuthority('SCOPE_read:resource')")
    public Resource indexResource(@PathVariable Long resource_id) {
        return resourceService.findById(resource_id);
    }

    //Post new resource
    @PostMapping("")
    @PreAuthorize("hasAuthority('SCOPE_create:resource')")
    public Resource createResource(@Valid @RequestBody Resource resource) {
        return resourceService.create(resource);
    }

    //update resource
    @PutMapping("/{resource_id}")
    @PreAuthorize("hasAuthority('SCOPE_update:resource')")
    public Resource updateResource(@PathVariable Long resource_id, @Valid @RequestBody Resource resourceRequest) {
        return resourceService.update(resource_id, resourceRequest);
    }

    //delete resource
    @DeleteMapping("/{resource_id}")
    @PreAuthorize("hasAuthority('SCOPE_delete:resource')")
    public ResponseEntity<?> deleteResource(@PathVariable Long resource_id) {
        resourceService.deleteById(resource_id);
        return ResponseEntity.ok()
                .build();
    }
}

