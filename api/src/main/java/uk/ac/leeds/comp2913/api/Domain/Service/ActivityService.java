package uk.ac.leeds.comp2913.api.Domain.Service;

import com.stripe.exception.StripeException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

public interface ActivityService {
 // Page<Activity> getActivities(Pageable pageable);

  ResponseEntity<?> deleteActivity(Long activity_id);

  Activity createNewActivity(Activity a, Long activity_type_id, RegularSession regularSession);

  Page<Activity> getActivities(Pageable pageable);

  Activity findActivityById(Long activity_id);

  Page<Activity> findByResourceId(Pageable pageable, Long resource_id);

  Activity editActivity(Long activity_id, Activity activityRequest);

  ResponseEntity<?> deleteRegularSession(Long regular_session_id);

  Page<Activity> findAllWithResources(Pageable pageable);

  void automatedRegularSessionAndBookings() throws StripeException;

  }