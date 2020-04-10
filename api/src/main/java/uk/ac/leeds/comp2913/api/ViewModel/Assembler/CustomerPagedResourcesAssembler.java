package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.CustomerController;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.ViewModel.CustomerDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerPagedResourcesAssembler extends RepresentationModelAssemblerSupport<Customer, CustomerDTO> {
    public CustomerPagedResourcesAssembler(){
        super(CustomerController.class, CustomerDTO.class);
    }

    public CustomerDTO toModel(Customer customer){
        CustomerDTO customerDTO = instantiateModel(customer);
        customerDTO.add(linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel());
        customerDTO.setId(customer.getId());
        customerDTO.setEmailAddress(customer.getEmailAddress());
        customerDTO.setDateOfBirth(customer.getDateOfBirth());
        return customerDTO;
    }

}
