package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Member;
import java.util.List;

import javax.transaction.Transactional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Service.AccountService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/*TODO: @API requirement, integrate this with Auth0, allow users to update their profile in the Frontend */
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    @Operation(summary = "Get all accounts",
            description = "Get list of all accounts")
    public CollectionModel<Account> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        for (Account account : accounts){
            Long accountId = account.getId();
            Link selfLink = linkTo(AccountController.class).slash(accountId).withSelfRel();
            account.add(selfLink);
        }
        Link viewAllAccounts = linkTo(AccountController.class).withSelfRel();
        CollectionModel<Account> result = new CollectionModel<>(accounts, viewAllAccounts);
        return result;
    }

    @GetMapping("/{account_id}")
    @Operation(summary = "Get specific account",
            description = "Get a specific account, links to view their customer details, bookings, membership")
    public Account getAccountById (@PathVariable Long account_id){
        Account account = accountService.getAccountById(account_id);
        Link accountCustomerDetailsLink = linkTo(AccountController.class).slash(account_id).slash("details").withRel("Customer Details");
        Link accountBookingsLink = linkTo(AccountController.class).slash(account_id).slash("bookings").withRel("Account Bookings");
        Link accountMembershipLink = linkTo(AccountController.class).slash(account_id).slash("membership").withRel("Account Memberships");
        account.add(accountCustomerDetailsLink, accountBookingsLink, accountMembershipLink);
        return account;
    }

   @GetMapping("/{account_id}/details")
    @Operation(summary = "View customer details",
           description = "View customer details for a particular account")
   public Customer getAccountCustomerDetails (@PathVariable Long account_id){
        Customer customer = accountService.getCustomerAccountDetails(account_id);
       Link accountLink = linkTo(AccountController.class).slash(account_id).withRel("Account");
       customer.add(accountLink);
       return customer;
   }

   @GetMapping("/{account_id}/bookings")
   @Operation(summary = "view bookings for an account",
           description = "Get list of all bookings linked to a specific account")
     public List<Booking> getAccountBookings (@PathVariable Long account_id){
         return accountService.getAccountBookings(account_id);
     }

    @GetMapping("/{account_id}/membership")
    @Operation(summary = "Get memberships for an account",
            description = "Get list of all memberships associated with an account")
    public List<Membership> getAccountMemberships (@PathVariable Long account_id){
        return accountService.getAccountMembership(account_id);
    }



}
