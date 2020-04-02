package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;

import java.util.Collection;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service
 * TODO: @CHORE, add HAL to all endpoints, with links to where the client can find
 * *          the associated resource, account and activity  for the booking
 * TODO: @CHORE, add hasAuthority checks to all endpoints
 */
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
    }
}

