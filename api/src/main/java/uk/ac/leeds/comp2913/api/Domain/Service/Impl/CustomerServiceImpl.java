package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable){
        return this.customerRepository.findAll(pageable);
    }

    @Override
    public Customer findById(Long customer_id){
        return this.customerRepository.findById(customer_id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for ID" + customer_id));
    }


    @Override
    public Customer getCustomerByEmailAddress(String emailAddress) {
        return this.customerRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public Customer getCustomerByStripeId(String stripeId) {
        return this.customerRepository.findByStripeId(stripeId);
    }
}
