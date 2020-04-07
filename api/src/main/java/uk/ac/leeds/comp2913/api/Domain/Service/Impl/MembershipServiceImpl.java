package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipTypeRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final MembershipTypeRepository membershipTypeRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository, MembershipTypeRepository membershipTypeRepository, AccountRepository accountRepository) {
        this.membershipRepository = membershipRepository;
        this.membershipTypeRepository = membershipTypeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Page<Membership> findAllMembers(Pageable pageable) {
        return membershipRepository.findAll(pageable);
    }

    @Override
    public List<MembershipType> findAllMembershipTypes() {
        return membershipTypeRepository.findAll();
    }

    @Override
    public MembershipType findMembershipTypeById(Long membership_type_id) {
        return membershipTypeRepository.findById(membership_type_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership type not found for ID" + membership_type_id));
    }

    @Override
    public Membership findMembershipById(Long membership_id) {
        return membershipRepository.findById(membership_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found for ID" + membership_id));
    }

    @Override
    public List<Membership> findMembershipsByMembershipType(Long membership_type_id) {
        return membershipRepository.findByMembershipTypeId(membership_type_id);
    }

    @Override
    public Membership addMember(Long account_id, Long membership_type_id, Membership membership) {
        MembershipType membershipType = membershipTypeRepository.findById(membership_type_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership type not found for ID" + membership_type_id));
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));

        membership.setMembershipType(membershipType);
        membership.setAccount(account);
        membership.setStartDate(new Date());
        return membershipRepository.save(membership);
    }

    @Override
    public Membership updateMembership(Long membership_id, Membership membershipRequest) {
        Membership membership = membershipRepository.findById(membership_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id " + membership_id));
        membership.setRepeatingPayment(membershipRequest.getRepeatingPayment());
        membership.setMembershipType(membershipRequest.getMembershipType());
        return membershipRepository.save(membership);
    }

    @Override
    public Membership stopRepeatPayment(Long membership_id) {
        Membership membership = membershipRepository.findById(membership_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id " + membership_id));
        if (membership.getRepeatingPayment() == true) {
            membership.setRepeatingPayment(false);
        }
        return membershipRepository.save(membership);
    }

    @Override
    public ResponseEntity<?> deleteMembership(Long membership_id) {
        return membershipRepository.findById(membership_id)
                .map(membership -> {
                    membershipRepository.delete(membership);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Membership not found with id" + membership_id));
    }

    @Override
    public void automatedMembershipRenewals() {
        List<Membership> lastPayments = membershipRepository.findLastWithRepeatPayments();
        for (Membership lastMembership : lastPayments) {
            Date now = new Date();
            Date paymentDueDate = new Date(now.getTime()-((24*60*60*1000) * 7)); //Takes payment 7 days before membership expires

            if(lastMembership.getEndDate().after(paymentDueDate) && lastMembership.getEndDate().before(now)){ //only takes payments that enter this window
                Membership renewedMembership = Membership.renewMembership(lastMembership);
                this.membershipRepository.save(renewedMembership);
            }
        }
    }
}