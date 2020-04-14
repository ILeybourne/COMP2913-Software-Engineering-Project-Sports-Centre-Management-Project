package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.MembershipTypeController;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipTypeRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipTypeDTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MembershipTypePagedResourcedAssembler extends RepresentationModelAssemblerSupport<MembershipType, MembershipTypeDTO> {
    public MembershipTypePagedResourcedAssembler(){
        super(MembershipTypeController.class, MembershipTypeDTO.class);
    }

    public MembershipTypeDTO toModel(MembershipType membershipType){
        MembershipTypeDTO membershipTypeDTO = instantiateModel(membershipType);
        membershipTypeDTO.add(linkTo(methodOn(MembershipTypeController.class).getMembershipTypeById(membershipType.getId())).withSelfRel());
        membershipTypeDTO.setId(membershipType.getId());
        membershipTypeDTO.setName(membershipType.getName());
        membershipTypeDTO.setCost(membershipType.getCost());
        membershipTypeDTO.setDuration(membershipType.getDuration());
        membershipTypeDTO.setCreatedAt(membershipType.getCreatedAt());
        membershipTypeDTO.setUpdatedAt(membershipType.getUpdatedAt());
        return membershipTypeDTO;
    }
}
