package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.AccountController;
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
        accountDTO.setId(account.getId());
        accountDTO.setCreated_at(account.getCreatedAt());
        accountDTO.setUpdated_at(account.getUpdatedAt());
        return accountDTO;
    }
}
