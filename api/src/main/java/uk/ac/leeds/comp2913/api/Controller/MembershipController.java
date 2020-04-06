package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipTypeRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/membership")
public class MembershipController {
    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
      this.membershipService = membershipService;
    }

    @GetMapping("/types")
    public CollectionModel<MembershipType> getMembershipTypes(Pageable pageable) {
        Page<MembershipType> allMembershipTypes = membershipService.findAllMembershipTypes(pageable);
        for(MembershipType membershipType : allMembershipTypes){
            Link selfLink = linkTo(MembershipController.class).slash("types").slash(membershipType.getId()).withSelfRel();
            Link membershipsLink = linkTo(MembershipController.class).slash("members").slash("type").slash(membershipType.getId()).withRel("All memberships with this type");
            membershipType.add(selfLink, membershipsLink);
        }
        Link allLink = linkTo(MembershipController.class).slash("types").withSelfRel();
        CollectionModel<MembershipType> result = new CollectionModel<>(allMembershipTypes, allLink);
        return result;
    }

    @GetMapping("/types/{membership_type_id}")
    public MembershipType getMembershipTypeById(@PathVariable Long membership_type_id) {
        MembershipType membershipType = membershipService.findMembershipTypeById(membership_type_id);
        Link selfLink = linkTo(MembershipController.class).slash("types").slash(membership_type_id).withSelfRel();
        membershipType.add(selfLink);

        return membershipType;
    }


  // get all memberships
    @GetMapping("/members")
    public CollectionModel<Membership> getMembers(Pageable pageable) {
        Page<Membership> allMembers = membershipService.findAllMembers(pageable);
        for(Membership membership : allMembers){
            Link selfLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withSelfRel();
            Link membershipTypeLink = linkTo(MembershipController.class).slash("types").slash(membership.getMembershipType().getId()).withRel("Membership Type");
            membership.add(selfLink, membershipTypeLink);
        }
        CollectionModel<Membership> result = new CollectionModel<>(allMembers);
        return result;
    }

    @GetMapping("/members/{membership_id}")
    public Membership getMembershipById(@PathVariable Long membership_id) {
        Membership membership = membershipService.findMembershipById(membership_id);
        Link selfLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withSelfRel();
        Link accountLink = linkTo(AccountController.class).slash(membership.getAccount().getId()).withRel("Account Details");
        Link membershipTypeLink = linkTo(MembershipController.class).slash("types").slash(membership.getMembershipType().getId()).withRel("Membership Type");
        Link updateLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withRel("update");
        Link deleteLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withRel("delete");
        membership.add(selfLink, accountLink, membershipTypeLink, updateLink, deleteLink);
        if(membership.getRepeatingPayment() == true){
            Link stopPaymentsLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).slash("stop").withRel("stop repeat payments");
            membership.add(stopPaymentsLink);
        }
        return membership;
    }

    @GetMapping("/members/type/{membership_type_id}")
    public CollectionModel<Membership> findAllMembershipsWithType(@PathVariable Long membership_type_id){
    List<Membership> allMembers = membershipService.findMembershipsByMembershipType(membership_type_id);
        for(Membership membership : allMembers){
        Link selfLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withSelfRel();
        Link membershipTypeLink = linkTo(MembershipController.class).slash("types").slash(membership.getMembershipType().getId()).withRel("Membership Type");
        membership.add(selfLink, membershipTypeLink);
    }
    CollectionModel<Membership> result = new CollectionModel<>(allMembers);
        return result;
}


    //Add a member, store membership with account Id and membership type id
    @PostMapping("")
    public Membership addMembership(@Valid @RequestBody MembershipDTO membership){
        Membership m = new Membership();
        m.setRepeatingPayment(membership.isRepeatingPayment());
        return membershipService.addMember(membership.getAccountId(), membership.getMembershipTypeId(), m);
    }

    // Upgrade/downgrade membership type
    @PutMapping("/members/{membership_id}")
    public Membership updateMembership(@PathVariable Long membership_id, @Valid @RequestBody Membership membershipRequest) {
        return membershipService.updateMembership(membership_id, membershipRequest);
    }

    @PutMapping("/members/{membership_id}/stop")
    public Membership stopRepeatingPayment(@PathVariable Long membership_id) {
        return membershipService.stopRepeatPayment(membership_id);
    }

    //cancel membership
    @DeleteMapping("/members/{membership_id}")
    public ResponseEntity<?> deleteMembership(@PathVariable Long membership_id) {
        return membershipService.deleteMembership(membership_id);
    }

}
