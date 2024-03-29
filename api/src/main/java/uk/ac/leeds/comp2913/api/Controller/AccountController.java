package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.ViewModel.AccountDTO;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.AccountPagedResourcesAssembler;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Service.AccountService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/*TODO: @API requirement, integrate this with Auth0, allow users to update their profile in the Frontend */
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountPagedResourcesAssembler accountPagedResourcesAssembler;
    private final PagedResourcesAssembler<Account> pagedResourcesAssembler;

    @Autowired
    public AccountController(AccountService accountService, AccountPagedResourcesAssembler accountPagedResourcesAssembler, PagedResourcesAssembler<Account> pagedResourcesAssembler) {
        this.accountService = accountService;
        this.accountPagedResourcesAssembler = accountPagedResourcesAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

   @GetMapping
   @Operation(summary = "Get all accounts",
           description = "Get list of all accounts")
   public PagedModel<AccountDTO> getAccounts(Pageable pageable) {
       return pagedResourcesAssembler.toModel((accountService.getAccounts(pageable)), accountPagedResourcesAssembler);
   }

  @GetMapping("/{account_id}")
  @Operation(summary = "Get specific account",
          description = "Get a specific account, links to view their customer details, bookings, membership")
  public AccountDTO getAccountById (@Parameter(description = "The ID of the specific account", required = true)@PathVariable Long account_id){
        return accountPagedResourcesAssembler.toModel(accountService.getAccountById(account_id));
  }
}
