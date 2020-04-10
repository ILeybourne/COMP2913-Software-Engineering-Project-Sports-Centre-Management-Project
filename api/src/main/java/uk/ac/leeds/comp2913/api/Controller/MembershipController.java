package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.web.PagedResourcesAssembler;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/membership")
public class MembershipController {
    private final MembershipService membershipService;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    public MembershipController(MembershipService membershipService, PagedResourcesAssembler pagedResourcesAssembler) {
      this.membershipService = membershipService;
      this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    //@GetMapping("/types")
    //@Operation(summary = "Get all membership types",
    //        description = "Get list of all available membership types" +
    //                "showing basic information")
    //public PagedModel<MembershipType> getMembershipTypes(Pageable pageable) {
    //    Page<MembershipType> allMembershipTypes = membershipService.findAllMembershipTypes(pageable);
    //    for(MembershipType membershipType : allMembershipTypes){
    //        Link selfLink = linkTo(MembershipController.class).slash("types").slash(membershipType.getId()).withSelfRel();
    //        Link membershipsLink = linkTo(MembershipController.class).slash("members").slash("type").slash(membershipType.getId()).withRel("All memberships with this type");
    //        membershipType.add(selfLink, membershipsLink);
    //    }
    //    Link allLink = linkTo(MembershipController.class).slash("types").withSelfRel();
    //    PagedModel<MembershipType> result = pagedResourcesAssembler.toModel(allMembershipTypes);
    //    result.add(allLink);
    //    return result;
    //}
//
    //@GetMapping("/types/{membership_type_id}")
    //@Operation(summary = "Get a specific membership type",
    //        description = "Get particular membership type with a link to view all members with this type")
    //public MembershipType getMembershipTypeById( @Parameter(description = "The ID of the membership type", required = true)@PathVariable Long membership_type_id) {
    //    MembershipType membershipType = membershipService.findMembershipTypeById(membership_type_id);
    //    Link selfLink = linkTo(MembershipController.class).slash("types").slash(membership_type_id).withSelfRel();
    //    Link addMembership = linkTo(MembershipController.class).slash(membership_type_id).withRel("add membership of this type");
    //    Link membershipsWithType = linkTo(MembershipController.class).slash("members").slash("type").slash(membership_type_id).withRel("Memberships with this type");
    //    membershipType.add(selfLink, membershipsWithType, addMembership);
    //    return membershipType;
    //}
//
//
  //// get all memberships
    //@GetMapping("/members")
    //@Operation(summary = "Get all members",
    //        description = "Get a list of all purchased memberships with basic information" +
    //                "REMOVE SORTING TO WORK")
    //public PagedModel<Membership> getMembers(Pageable pageable) {
    //    Page<Membership> allMembers = membershipService.findAllMembers(pageable);
    //    for(Membership membership : allMembers){
    //        Link selfLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withSelfRel();
    //        Link membershipTypeLink = linkTo(MembershipController.class).slash("types").slash(membership.getMembershipType().getId()).withRel("Membership Type");
    //        membership.add(selfLink, membershipTypeLink);
    //    }
    //    PagedModel<Membership> result = pagedResourcesAssembler.toModel(allMembers);
    //    return result;
    //}
//
    //@GetMapping("/members/{membership_id}")
    //@Operation(summary = "Get specific membership",
    //        description = "Get a specific membership with details/links")
    //public Membership getMembershipById( @Parameter(description = "The ID of the membership", required = true)@PathVariable Long membership_id) {
    //    Membership membership = membershipService.findMembershipById(membership_id);
    //    Link selfLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withSelfRel();
    //    Link accountLink = linkTo(AccountController.class).slash(membership.getAccount().getId()).withRel("Account Details");
    //    Link membershipTypeLink = linkTo(MembershipController.class).slash("types").slash(membership.getMembershipType().getId()).withRel("Membership Type");
    //    Link updateLink = linkTo(MembershipController.class).slash(membership.getId()).withRel("update");
    //    Link deleteLink = linkTo(MembershipController.class).slash(membership.getId()).withRel("delete");
    //    membership.add(selfLink, accountLink, membershipTypeLink, updateLink, deleteLink);
    //    if(membership.getRepeatingPayment() == true){
    //        Link stopPaymentsLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).slash("stop").withRel("stop auto renewal");
    //        membership.add(stopPaymentsLink);
    //    }
    //    return membership;
    //}
//
    //@GetMapping("/members/type/{membership_type_id}")
    //@Operation(summary = "Get Memberships by a particular membership type",
    //        description = "Get list a list of memberships for a particular type (all monthly memberships etc)")
    //public PagedModel<Membership> findAllMembershipsWithType(Pageable pageable, @Parameter(description = "The ID of the membership type", required = true)@PathVariable Long membership_type_id){
    //Page<Membership> allMembers = membershipService.findMembershipsByMembershipType(pageable, membership_type_id);
    //    for(Membership membership : allMembers){
    //    Link selfLink = linkTo(MembershipController.class).slash("members").slash(membership.getId()).withSelfRel();
    //    Link membershipTypeLink = linkTo(MembershipController.class).slash("types").slash(membership.getMembershipType().getId()).withRel("Membership Type");
    //    membership.add(selfLink, membershipTypeLink);
    //    }
    //    PagedModel<Membership> result = pagedResourcesAssembler.toModel(allMembers);
    //    return result;
    //}
//

    //Add a member, store membership with account Id and membership type id
    @PostMapping("/{membership_type_id}")
    @Operation(summary = "Take out a membership #3",
            description = "Purchase a membership, requires membership type, account and whether the payment should repeat")
    public Membership addMembership( @Parameter(description = "A membership DTO Object", required = true)@Valid @RequestBody MembershipDTO membership,
                                     @Parameter(description = "The membership type Id", required = true)@PathVariable Long membership_type_id){
        Membership m = new Membership();
        m.setRepeatingPayment(membership.isRepeatingPayment());
        return membershipService.addMember(membership.getAccountId(), membership_type_id, m);
    }

    // Upgrade/downgrade membership type
    @PutMapping("/{membership_id}")
    @Operation(summary = "Edit the details of a membership",
            description = "Update the fields of a particular membership")
    public Membership updateMembership( @Parameter(description = "The Id of the membership", required = true)@PathVariable Long membership_id,
                                        @Parameter(description = "A Membership Object", required = true)@Valid @RequestBody Membership membershipRequest) {
        return membershipService.updateMembership(membership_id, membershipRequest);
    }

    @PutMapping("/members/{membership_id}/stop")
    @Operation(summary = "Stop the repeating payment of a membership",
            description = "Cancel the repeating payment of a membership, changing the boolean to false")
    public Membership stopRepeatingPayment( @Parameter(description = "The ID of the membership", required = true)@PathVariable Long membership_id) {
        return membershipService.stopRepeatPayment(membership_id);
    }

    //cancel membership
    @DeleteMapping("/{membership_id}")
    @Operation(summary = "Cancel the membership",
            description = "Cancels a membership (removes from database/cancels sale")
    public ResponseEntity<?> deleteMembership( @Parameter(description = "The ID of the membership", required = true)@PathVariable Long membership_id) {
        return membershipService.deleteMembership(membership_id);
    }

}
