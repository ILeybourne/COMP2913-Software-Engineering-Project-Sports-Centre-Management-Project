package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.core.Relation;
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
@Relation(collectionRelation = "resources", itemRelation = "resource")
public class FacilityDTOAssembler extends RepresentationModelAssemblerSupport<Resource, ResourceDTO> {
    public FacilityDTOAssembler(){
        super(ResourceController.class, ResourceDTO.class);
    }

    public ResourceDTO toModel(Resource resource){
        ResourceDTO resourceDTO = instantiateModel(resource);

        resourceDTO.add(linkTo(methodOn(ResourceController.class)
                .getResourceById(resource.getId()))
                .withSelfRel());

        resourceDTO.add(linkTo(ActivityTypeController.class)
                .slash("resource")
                .slash(resource.getId())
                .withRel("View Activity Types for Facility"));
        resourceDTO.add(linkTo(TimetableController.class)
                .slash(resource.getId())
                .withRel("Facility Timetable"));
        resourceDTO.add(linkTo(ResourceController.class)
                .slash(resource.getId())
                .withRel("update"));
        resourceDTO.add(linkTo(ResourceController.class)
                .slash(resource.getId())
                .withRel("delete"));
        resourceDTO.add(linkTo(ActivityTypeController.class)
                .slash("resource")
                .slash(resource.getId())
                .withRel("Create new Activity Type for Facility"));
        resourceDTO.add(linkTo(ResourceController.class)
                .withRel("Create new resource"));

        if (resource.getFilePath() != null) {
            resourceDTO.add(linkTo(ResourceController.class)
                    .slash("image")
                    .slash(resource.getId())
                    .withRel("View image for resource"));
        }

        resourceDTO.setId(resource.getId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setCreatedAt(resource.getCreatedAt());
        resourceDTO.setUpdatedAt(resource.getUpdatedAt());
        resourceDTO.setDescription(resource.getDescription());

        return resourceDTO;
    }
}
