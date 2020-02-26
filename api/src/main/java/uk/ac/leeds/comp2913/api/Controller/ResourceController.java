package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@RestController
@RequestMapping("/resources")
public class ResourceController {

  private final ResourceRepository resourceRepository;

  public ResourceController(ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

  @GetMapping("/")
  public Page<Resource> getResources(Pageable pageable) {
    return resourceRepository.findAll(pageable);
  }

  @PostMapping("/")
  public Resource createResource(@Valid @RequestBody Resource resource) {
    return resourceRepository.save(resource);
  }

  @PutMapping("/{resource_id}")
  public Resource updateResource(@PathVariable Long resource_id, @Valid @RequestBody Resource resourceRequest) {
    return resourceRepository.findById(resource_id)
      .map(resource -> {
        resource.setName(resourceRequest.getName());
        resource.setActivities(resourceRequest.getActivities());
        return resourceRepository.save(resource);
      }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
  }

  @DeleteMapping("/{resource_id}")
  public ResponseEntity<?> deleteResource(@PathVariable Long resource_id) {
    return resourceRepository.findById(resource_id)
      .map(resource -> {
        resourceRepository.delete(resource);
        return ResponseEntity.ok().build();
      }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
  }
}

