package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

public interface ActivityService {
 // Page<Activity> getActivities(Pageable pageable);

  void schedule();

  ResponseEntity<?> deleteActivity(Long activity_id);

  Activity createNewActivity(Activity a, Long activity_type_id, RegularSession regularSession);

  List<Activity> getActivities();

  Activity findActivityById(Long activity_id);

  List<Activity> findByResourceId(Long resource_id);

  Activity editActivity(Long activity_id, Activity activityRequest);

  ResponseEntity<?> deleteRegularSession(Long regular_session_id);

    Collection<Activity> findAllWithResources();
}