package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.ActivityTypeController;
import uk.ac.leeds.comp2913.api.Controller.ResourceController;
import uk.ac.leeds.comp2913.api.Controller.TimetableController;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.ViewModel.ResourceDTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResourcePagedResourcesAssembler extends RepresentationModelAssemblerSupport<Resource, ResourceDTO> {
    public ResourcePagedResourcesAssembler(){
        super(ResourceController.class, ResourceDTO.class);
    }

    public ResourceDTO toModel(Resource resource){
        ResourceDTO resourceDTO = instantiateModel(resource);
        resourceDTO.add(linkTo(methodOn(ResourceController.class).getResourceById(resource.getId())).withSelfRel());
        resourceDTO.add(linkTo(ActivityTypeController.class).slash("resource").slash(resource.getId()).withRel("View Activity Types for Resource"));
        resourceDTO.add(linkTo(TimetableController.class).slash(resource.getId()).withRel("Facility Timetable"));

        resourceDTO.setId(resource.getId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setCreatedAt(resource.getCreatedAt());
        resourceDTO.setUpdatedAt(resource.getUpdatedAt());
        return resourceDTO;
    }
}
