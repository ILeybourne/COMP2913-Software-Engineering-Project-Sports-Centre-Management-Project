package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.ActivityController;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActivityDTOAssembler extends RepresentationModelAssemblerSupport<Activity, ActivityDTO> {

    public ActivityDTOAssembler() {
        super(ActivityController.class, ActivityDTO.class);
    }

    @Override
    public ActivityDTO toModel(Activity activity) {
        ActivityDTO activityDTO = instantiateModel(activity);
        activityDTO.add(linkTo(methodOn(ActivityController.class).getActivityByActivityId(activity.getId())).withSelfRel());
        activityDTO.setId(activity.getId());
        activityDTO.setName(activity.getName());
        activityDTO.setStartTime(activity.getStartTime());
        activityDTO.setEndTime(activity.getEndTime());
        activityDTO.setCost(activity.getCost());
        activityDTO.setCurrentCapacity(activity.getCurrentCapacity());
        activityDTO.setTotalCapacity(activity.getActivityType().getTotalCapacity());
        activityDTO.setRegularSessionId(activity.getRegularSession());
        activityDTO.setSocial(activity.getSocial());
        activityDTO.setResource(activity.getResource());
        activityDTO.setActivityTypeId(activity.getActivityType().getId());
        return activityDTO;
    }
}
