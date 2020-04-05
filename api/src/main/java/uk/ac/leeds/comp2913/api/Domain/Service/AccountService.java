package uk.ac.leeds.comp2913.api.Domain.Service;

import java.util.List;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;

public interface AccountService {

    @Transactional
    List<Account> getAccounts();

    @Transactional
    Account getAccountById(Long account_id);

    @Transactional
    Customer getCustomerAccountDetails(Long account_id);

    @Transactional
    List<Booking> getAccountBookings(Long account_id);

    @Transactional
    List<Membership> getAccountMembership(Long account_id);
}
