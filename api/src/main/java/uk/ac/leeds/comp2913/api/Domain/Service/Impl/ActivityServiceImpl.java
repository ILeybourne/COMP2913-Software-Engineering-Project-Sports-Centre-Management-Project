package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {
  private final ActivityRepository activityRepository;

  @Autowired
  public ActivityServiceImpl(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;

  }

  @Override
  public Page<Activity> getActivities(Pageable pageable) {
    return activityRepository.findAll(pageable);
  }

  @Override
  public ActivityDTO getCapacityForActivityId(Long activityId) {
    return activityRepository.calculateCurrentCapacity(activityId);
  }
}
