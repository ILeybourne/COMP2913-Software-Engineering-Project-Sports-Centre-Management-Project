package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;

import java.util.Collection;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

  private final ActivityRepository activityRepository;

  @Autowired
  public TimetableController(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  @GetMapping("")
  public Collection<Activity> getResources() {
    return activityRepository.findAllWithResources();
//      .stream()
//      .map(ActivityToTimetableDTOAdapter::map)
//      .collect(Collectors.toList());
  }
}

