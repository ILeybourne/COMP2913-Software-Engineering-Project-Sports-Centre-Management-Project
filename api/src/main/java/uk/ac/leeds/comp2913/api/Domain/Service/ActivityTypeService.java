package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;

public interface ActivityTypeService {

    Page<ActivityType> findAll(Pageable pageable);

    Page<ActivityType> findByResourceId(Pageable pageable, Long resource_id);

    ActivityType addActivityType(Long resource_id, ActivityType activityTypeRequest);

    ActivityType updateActivityType(Long activity_type_id, ActivityType activityTypeRequest);

    ResponseEntity<?> deleteActivityType(Long activity_type_id);

    ActivityType findById(Long activity_type_id);
}
