package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@RestController
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping("/resources/{resource_id}/activities")
    public List<Activity> getActivitiesByResourceId(@PathVariable Long resource_id) {
        return activityRepository.findByResourceId(resource_id);
    }

    @PostMapping("/resources/{resource_id}/activities")
    public Activity AddActivity(@PathVariable Long resource_id,
                            @Valid @RequestBody Activity activity) {
        return resourceRepository.findById(resource_id)
                .map(resource -> {
                    activity.getResource(resource_id);
                    return activityRepository.save(activity);
                }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
    }

    @PutMapping("/resources/{resource_id}/activities/{activity_id}")
    public Activity updateActivity(@PathVariable Long resource_id,
                               @PathVariable Long activity_id,
                               @Valid @RequestBody Activity activityRequest) {
        if(!resourceRepository.existsById(resource_id)) {
            throw new ResourceNotFoundException("Resource not found with id " + resource_id);
        }

        return activityRepository.findById(activity_id)
                .map(activity -> {
                    activity.setName(activityRequest.getName());
                    return activityRepository.save(activity);
                }).orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + activity_id));
    }

    @DeleteMapping("/resources/{resource_id}/activities/{activity_id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long resource_id,
                                          @PathVariable Long activity_id) {
        if(!resourceRepository.existsById(resource_id)) {
            throw new ResourceNotFoundException("Resource not found with id " + resource_id);
        }

        return activityRepository.findById(activity_id)
                .map(activity -> {
                    activityRepository.delete(activity);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + activity_id));

    }
}