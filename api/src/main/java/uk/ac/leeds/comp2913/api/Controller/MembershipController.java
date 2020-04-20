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
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.MembershipPagedResourcesAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/membership")
public class MembershipController {
    private final MembershipService membershipService;
    private final PagedResourcesAssembler<Membership> pagedResourcesAssembler;
    private final MembershipPagedResourcesAssembler membershipPagedResourcesAssembler;

    @Autowired
    public MembershipController(MembershipService membershipService, PagedResourcesAssembler<Membership> pagedResourcesAssembler, MembershipPagedResourcesAssembler membershipPagedResourcesAssembler) {
      this.membershipService = membershipService;
      this.pagedResourcesAssembler = pagedResourcesAssembler;
      this.membershipPagedResourcesAssembler = membershipPagedResourcesAssembler;
    }

  // get all memberships
    @GetMapping("/members")
    @Operation(summary = "Get all members",
            description = "Get a list of all purchased memberships with basic information" +
                    "REMOVE SORTING TO WORK")
    public PagedModel<MembershipDTO> getMembers(Pageable pageable) {
        Page<Membership> allMembers = membershipService.findAllMembers(pageable);
        return pagedResourcesAssembler.toModel(allMembers, membershipPagedResourcesAssembler);
    }

    @GetMapping("/members/{membership_id}")
    @Operation(summary = "Get specific membership",
            description = "Get a specific membership with details/links")
    public MembershipDTO getMembershipById( @Parameter(description = "The ID of the membership", required = true)@PathVariable Long membership_id) {
        MembershipDTO membership =membershipPagedResourcesAssembler.toModel(membershipService.findMembershipById(membership_id));
        membership.add(linkTo(MembershipController.class).slash(membership.getId()).withRel("update"));
        membership.add(linkTo(MembershipController.class).slash(membership.getId()).withRel("delete"));
        return membership;
    }

    @GetMapping("/members/account/{account_id}")
    @Operation(summary = "Get memberships by account",
            description = "Get memberships by account id")
    public PagedModel<MembershipDTO> getMembershipByAccountId(Pageable pageable, @Parameter(description = "The ID of the Account", required = true)@PathVariable Long account_id) {
        Page <Membership> accountMemberships = membershipService.findMembershipByAccountId(pageable, account_id);
        return pagedResourcesAssembler.toModel(accountMemberships, membershipPagedResourcesAssembler);
    }

    @GetMapping("/members/type/{membership_type_id}")
    @Operation(summary = "Get Memberships by a particular membership type",
            description = "Get list a list of memberships for a particular type (all monthly memberships etc)")
    public PagedModel<MembershipDTO> findAllMembershipsWithType(Pageable pageable, @Parameter(description = "The ID of the membership type", required = true)@PathVariable Long membership_type_id){
    Page<Membership> allMembers = membershipService.findMembershipsByMembershipType(pageable, membership_type_id);
        return pagedResourcesAssembler.toModel(allMembers, membershipPagedResourcesAssembler);
    }


    //Add a member, create an account in the database and store customer details
    @PostMapping("/{membership_type_id}")
    @Operation(summary = "Take out a membership #3",
            description = "Purchase a membership, requires membership type, account and whether the payment should repeat")
    public Membership addMembership( @Parameter(description = "A membership DTO Object", required = true)@Valid @RequestBody MembershipDTO membership,
                                     @Parameter(description = "The membership type Id", required = true)@PathVariable Long membership_type_id){
        Membership m = new Membership();
        Customer c = new Customer();
        Account a = new Account();
        c.setDateOfBirth(membership.getDateOfBirth());
        c.setEmailAddress(membership.getEmailAddress());
        a.setCustomer(c);
        m.setRepeatingPayment(membership.isRepeatingPayment());
        return membershipService.addMember(membership_type_id, m, a, c);
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
    public void stopRepeatingPayment( @Parameter(description = "The ID of the membership", required = true)@PathVariable Long membership_id) {
        membershipService.stopRepeatPayment(membership_id);
    }

    //cancel membership
    @DeleteMapping("/{membership_id}")
    @Operation(summary = "Cancel the membership",
            description = "Cancels a membership (removes from database/cancels sale")
    public ResponseEntity<?> deleteMembership( @Parameter(description = "The ID of the membership", required = true)@PathVariable Long membership_id) {
        return membershipService.deleteMembership(membership_id);
    }

}
