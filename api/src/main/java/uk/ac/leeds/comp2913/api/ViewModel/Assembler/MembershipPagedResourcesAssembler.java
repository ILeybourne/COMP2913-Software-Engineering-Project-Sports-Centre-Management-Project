package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.AccountController;
import uk.ac.leeds.comp2913.api.Controller.MembershipController;
import uk.ac.leeds.comp2913.api.Controller.MembershipTypeController;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MembershipPagedResourcesAssembler extends RepresentationModelAssemblerSupport<Membership, MembershipDTO> {
    public MembershipPagedResourcesAssembler(){
        super(MembershipController.class, MembershipDTO.class);
    }
    public MembershipDTO toModel (Membership membership){
        MembershipDTO membershipDTO = instantiateModel(membership);
        membershipDTO.add(linkTo(methodOn(MembershipController.class).getMembershipById(membership.getId())).withSelfRel());
        membershipDTO.add(linkTo(methodOn(AccountController.class).getAccountById(membership.getAccount().getId())).withRel("Account"));
        membershipDTO.add(linkTo(methodOn(MembershipTypeController.class).getMembershipTypeById(membership.getMembershipType().getId())).withRel("Membership Type"));

        membershipDTO.setId(membership.getId());
        membershipDTO.setName(membership.getName());
        membershipDTO.setStartDate(membership.getStartDate());
        membershipDTO.setEndDate(membership.getEndDate());
        membershipDTO.setRepeatingPayment(membership.getRepeatingPayment());
        membershipDTO.setCreated_at(membership.getCreatedAt());
        membershipDTO.setUpdated_at(membership.getUpdatedAt());
        if(membership.getRepeatingPayment() == true){
            membershipDTO.add(linkTo(MembershipController.class).slash("members").slash(membership.getId()).slash("stop").withRel("stop auto renewal"));
        }
        membershipDTO.setAccountId(membership.getAccount().getId());
        membershipDTO.setMembershipTypeId(membership.getMembershipType().getId());
        return membershipDTO;
    }
}
