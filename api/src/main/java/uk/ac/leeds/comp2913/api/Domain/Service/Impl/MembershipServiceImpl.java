package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Member;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Controller.PaymentController;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipTypeRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final MembershipTypeRepository membershipTypeRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    Logger logger = LoggerFactory.getLogger(MembershipServiceImpl.class);


    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository, MembershipTypeRepository membershipTypeRepository, AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.membershipRepository = membershipRepository;
        this.membershipTypeRepository = membershipTypeRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Page<Membership> findAllMembers(Pageable pageable) {
        return membershipRepository.findAll(pageable);
    }


    @Override
    public Membership findMembershipById(Long membership_id) {
        return membershipRepository.findById(membership_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found for ID" + membership_id));
    }

    @Override
    public Page<Membership> findMembershipsByMembershipType(Pageable pageable, Long membership_type_id) {
        return membershipRepository.findByMembershipTypeId(pageable, membership_type_id);
    }

    @Override
    public Page<Membership> findMembershipByAccountId(Pageable pageable, Long account_id){
        return membershipRepository.findByAccountId(pageable, account_id);
    }

    //Checks existing customers don't have active memberships
    @Override
    @Transactional
    public Boolean activeMemberCheck(String email) {
        Boolean activeMember = false;
        Customer existingCustomer = customerRepository.findByEmailAddress(email);
        logger.info(email);
        if(existingCustomer != null) {
            List<Account> customerAccounts = accountRepository.findAllByCustomerId(existingCustomer.getId());
            if (customerAccounts.size() > 0) {
                Account lastAccount = customerAccounts.get(customerAccounts.size() - 1);
                logger.info(lastAccount.toString());
                List<Membership> allMemberships = membershipRepository.findAllByAccountIdOrderByEndDateAsc(lastAccount.getId());
                if (allMemberships.size() > 0) {
                    Membership lastMembership = allMemberships.get(allMemberships.size() - 1);
                    Date lastMembershipEndDate = lastMembership.getEndDate();
                    logger.info(lastMembershipEndDate.toString());
                    if (lastMembershipEndDate.after(new Date())) { //checks to make sure old membership has expired
                        activeMember = true;
                    }
                }
            }
        }
        logger.info(activeMember.toString());
        return activeMember;
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
    @Transactional
    public void stopRepeatPayment(Long membership_id) {
        Membership m = membershipRepository.findById(membership_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found for ID" + membership_id));
        Long membership_type_id = m.getMembershipType().getId();
        Long account_id = m.getAccount().getId();
        membershipRepository.stopRepeatPayment(membership_type_id, account_id);

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