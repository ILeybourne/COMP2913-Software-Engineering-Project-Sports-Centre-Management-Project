package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Service.AccountService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long account_id){
        return accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));

    }

    @Override
    @Transactional
    public Customer getCustomerAccountDetails(Long account_id){
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));
        Customer customerDetails = account.getCustomer();
        Hibernate.initialize(customerDetails);
        return customerDetails;
    }

    @Override
    @Transactional
    public List <Booking> getAccountBookings(Long account_id){
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));
        List<Booking> accountBookings = account.getBookings();
        Hibernate.initialize(accountBookings);
        return accountBookings;
    }

    @Override
    @Transactional
    public List <Membership> getAccountMembership(Long account_id){
        Account account = accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));
        List <Membership> accountMembership = account.getMemberships();
        Hibernate.initialize(accountMembership);
        return accountMembership;
    }
}
