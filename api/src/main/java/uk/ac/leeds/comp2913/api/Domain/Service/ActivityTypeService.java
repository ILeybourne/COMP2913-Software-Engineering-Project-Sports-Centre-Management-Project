package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;

public interface ActivityTypeService {

    List<ActivityType> findAll();

    List<ActivityType> findByResourceId(Long resource_id);

    ActivityType addActivityType(Long resource_id, ActivityType activityTypeRequest);

    ActivityType updateActivityType(Long activity_type_id, ActivityType activityTypeRequest);

    ResponseEntity<?> deleteActivityType(Long activity_type_id);

    }
