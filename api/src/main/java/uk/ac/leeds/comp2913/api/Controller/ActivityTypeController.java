package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;


@RestController
public class ActivityTypeController {

    private final ResourceRepository resourceRepository;

    private final ActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityTypeController(ActivityTypeRepository activityTypeRepository, ResourceRepository resourceRepository) {
        this.activityTypeRepository = activityTypeRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Get all activity details required to schedule an activity in the database
     *
     */

    @GetMapping("/activitytypes")
    public List<ActivityType> getActivityTypes() {
        return activityTypeRepository.findAll();
    }

    //Get activity types for a resource
    @GetMapping("/resources/{resource_id}/activitytypes")
    public List<ActivityType> getActivitiesByResourceId(@PathVariable Long resource_id) {
        return activityTypeRepository.findByResourceId(resource_id);
    }

    //add new activity type to resource
    @PostMapping("/resources/{resource_id}/activitytypes")
    public ActivityType addActivityType(@PathVariable Long resource_id, @Valid @RequestBody ActivityType activityType) {
        return resourceRepository.findById(resource_id)
                .map(resource -> {
                    activityType.setResource(resource);
                    return activityTypeRepository.save(activityType);
                }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
    }

    //update activity
    @PutMapping("/activitytypes/{activity_type_id}")
    public ActivityType updateActivityType(@PathVariable Long activity_type_id, @Valid @RequestBody ActivityType activityTypeRequest) {

        return activityTypeRepository.findById(activity_type_id)
                .map(activityType -> {
                    activityType.setName(activityTypeRequest.getName());
                    activityType.setTotalCapacity(activityTypeRequest.getTotalCapacity());
                    activityType.setCost(activityTypeRequest.getCost());
//                    TODO: Others
                    return activityTypeRepository.save(activityType);
                }).orElseThrow(() -> new ResourceNotFoundException("Activity Type not found with ID " + activity_type_id));
    }

    //delete activity
    @DeleteMapping("/activitytypes/{activity_type_id}")
    public ResponseEntity<?> deleteActivityType(@PathVariable Long activity_type_id) {

        try {
            activityTypeRepository.deleteById(activity_type_id);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Activity not found with the ID " + activity_type_id);
        }
    }
}
