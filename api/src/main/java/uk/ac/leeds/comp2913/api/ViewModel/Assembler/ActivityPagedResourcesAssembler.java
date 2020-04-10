package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.ActivityController;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActivityPagedResourcesAssembler extends RepresentationModelAssemblerSupport<Activity, ActivityDTO> {

    public ActivityPagedResourcesAssembler() {
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
        //activityDTO.setSocial(activity.Social());
        activityDTO.setResource(activity.getResource());
        activityDTO.setCurrentCapacity(activity.getCurrentCapacity());
        activityDTO.setRegularSessionId(activity.getRegularSession());
        return activityDTO;
    }
}
