package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipTypeRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@RestController
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MembershipTypeRepository membershipTypeRepository;

    // get all memberships
    @GetMapping("/members")
    public Page<Membership> getMemberships(Pageable pageable) {
        return membershipRepository.findAll(pageable);
    }

    //Add a member, store membership with account Id and membership type id
    @PostMapping("/add/{account_id}/{membership_type_id}")
    public Membership addMembership(@PathVariable Long account_id,
                                    @PathVariable Long membership_type_id,
                                    @Valid @RequestBody Membership membership){
        return accountRepository.findById(account_id)
                .map(account -> {
                    membership.setAccount(membership.getAccount());
                    return membershipTypeRepository.findById(membership_type_id)
                            .map(membershipType -> {
                                membership.setMembershipType(membership.getMembershipType());
                                return membershipRepository.save(membership);
                            }) .orElseThrow(() -> new ResourceNotFoundException("Membership Type not found with id " + membership_type_id));
                }) .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + account_id));
    }


    // Upgrade/downgrade membership type
    @PutMapping("/{membership_id}")
    public Membership updateMembership(@PathVariable Long membership_id, @Valid @RequestBody Membership membershipRequest) {
        return membershipRepository.findById(membership_id)
                .map(membership -> {
                    membership.setMembershipType(membershipRequest.getMembershipType());
                    return membershipRepository.save(membership);
                }).orElseThrow(() -> new ResourceNotFoundException("Membership not found with id " + membership_id));
    }



    //cancel membership
    @DeleteMapping("/{membership_id}")
    public ResponseEntity<?> deleteMembership(@PathVariable Long membership_id) {
        return membershipRepository.findById(membership_id)
                .map(membership -> {
                    membershipRepository.delete(membership);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Membership not found with id" + membership_id));
    }

}
