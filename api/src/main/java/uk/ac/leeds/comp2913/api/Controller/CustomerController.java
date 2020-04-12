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
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.CustomerPagedResourcesAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.CustomerDTO;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final CustomerPagedResourcesAssembler customerPagedResourcesAssembler;

    @Autowired
    public CustomerController(CustomerService customerService, PagedResourcesAssembler<Customer> pagedResourcesAssembler, CustomerPagedResourcesAssembler customerPagedResourcesAssembler){
        this.customerService = customerService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.customerPagedResourcesAssembler = customerPagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all customers",
            description = "Get list of all customers")
    public PagedModel<CustomerDTO> getCustomers(Pageable pageable){
        Page<Customer> customers = customerService.findAll(pageable);
        return pagedResourcesAssembler.toModel(customers, customerPagedResourcesAssembler);
    }

    @GetMapping("/{customer_id}")
    @Operation(summary = "Get a customer",
            description = "Get a customer" )
    public CustomerDTO getCustomerById(@PathVariable Long customer_id){
        CustomerDTO customer = customerPagedResourcesAssembler.toModel(customerService.findById(customer_id));
        return customer;
    }


}
