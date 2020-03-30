package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import java.util.Optional;

public interface ActivityService {
  Page<Activity> getActivities(Pageable pageable);

 // ActivityDTO getCapacityForActivityId(Long activityId);

  void schedule();

}