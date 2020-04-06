package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityTypeRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService{
    private final ActivityTypeRepository activityTypeRepository;
    private final ResourceRepository resourceRepository;

    public ActivityTypeServiceImpl(ActivityTypeRepository activityTypeRepository, ResourceRepository resourceRepository){
        this.activityTypeRepository = activityTypeRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public List<ActivityType> findAll(){
        return activityTypeRepository.findAll();
    }

    @Override
    public ActivityType findById(Long activity_type_id){
        return activityTypeRepository.findById(activity_type_id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity Type not found with id " + activity_type_id));
    }

    @Override
    public List<ActivityType> findByResourceId(Long resource_id){
        return activityTypeRepository.findByResourceId(resource_id);
    }

    @Override
    public ActivityType addActivityType(Long resource_id, ActivityType activityType){
        return resourceRepository.findById(resource_id)
                .map(resource -> {
                    activityType.setResource(resource);
                    return activityTypeRepository.save(activityType);
                }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
    }

    @Override
    public ActivityType updateActivityType(Long activity_type_id, ActivityType activityTypeRequest){
        return activityTypeRepository.findById(activity_type_id)
                .map(activityType -> {
//                    TODO: @Chore Cleanup, let JsonCretor take care
                    activityType.setName(activityTypeRequest.getName());
                    activityType.setTotalCapacity(activityTypeRequest.getTotalCapacity());
                    activityType.setCost(activityTypeRequest.getCost());
                    return activityTypeRepository.save(activityType);
                }).orElseThrow(() -> new ResourceNotFoundException("Activity Type not found with ID " + activity_type_id));
    }

    @Override
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