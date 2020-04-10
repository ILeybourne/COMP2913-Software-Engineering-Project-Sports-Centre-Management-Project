package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.ActivityTypeController;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityTypeDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActivityTypePagedResourcesAssembler extends RepresentationModelAssemblerSupport<ActivityType, ActivityTypeDTO> {
    public ActivityTypePagedResourcesAssembler(){
        super(ActivityTypeController.class, ActivityTypeDTO.class);
    }
    @Override
    public ActivityTypeDTO toModel(ActivityType activityType){
        ActivityTypeDTO activityTypeDTO = instantiateModel(activityType);
        activityTypeDTO.add(linkTo(methodOn(ActivityTypeController.class).getActivityTypeId(activityType.getId())).withSelfRel());
        activityTypeDTO.setId(activityType.getId());
        activityTypeDTO.setName(activityType.getName());
        activityTypeDTO.setCost(activityType.getCost());
        activityTypeDTO.setTotalCapacity(activityType.getTotalCapacity());
        activityTypeDTO.setCreated_at(activityTypeDTO.getCreated_at());
        activityTypeDTO.setUpdated_at(activityType.getUpdatedAt());
        return activityTypeDTO;
    }
}

