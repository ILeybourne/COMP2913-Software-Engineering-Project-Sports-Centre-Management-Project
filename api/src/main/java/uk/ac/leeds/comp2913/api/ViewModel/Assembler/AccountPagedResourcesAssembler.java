package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.AccountController;
import uk.ac.leeds.comp2913.api.Controller.BookingController;
import uk.ac.leeds.comp2913.api.Controller.CustomerController;
import uk.ac.leeds.comp2913.api.Controller.MembershipController;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.ViewModel.AccountDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AccountPagedResourcesAssembler extends RepresentationModelAssemblerSupport<Account, AccountDTO> {

    public AccountPagedResourcesAssembler(){
        super(AccountController.class, AccountDTO.class);
    }
    @Override
    public AccountDTO toModel(Account account){
        AccountDTO accountDTO = instantiateModel(account);
        accountDTO.add(linkTo(methodOn(AccountController.class).getAccountById(account.getId())).withSelfRel());
        accountDTO.add(linkTo(methodOn(CustomerController.class).getCustomerById(account.getCustomer().getId())).withRel("Customer Details"));
        accountDTO.add(linkTo(BookingController.class).slash("account").slash(account.getId()).withRel("Account Bookings"));
        accountDTO.add(linkTo(MembershipController.class).slash("members").slash("account").slash(account.getId()).withRel("Account Memberships"));
        accountDTO.setId(account.getId());
        accountDTO.setCreated_at(account.getCreatedAt());
        accountDTO.setUpdated_at(account.getUpdatedAt());
        return accountDTO;
    }
}
