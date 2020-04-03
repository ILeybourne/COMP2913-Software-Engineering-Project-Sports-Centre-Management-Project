package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.stereotype.Service;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerByEmailAddress(String emailAddress) {
        return this.customerRepository.findByEmailAddress(emailAddress);
    }
}
