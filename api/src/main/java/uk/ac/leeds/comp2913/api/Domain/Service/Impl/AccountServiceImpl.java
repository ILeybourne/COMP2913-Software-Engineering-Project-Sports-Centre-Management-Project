package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Account> getAccounts(Pageable pageable){
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account getAccountById(Long account_id){
        return accountRepository.findById(account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID" + account_id));

    }

}
