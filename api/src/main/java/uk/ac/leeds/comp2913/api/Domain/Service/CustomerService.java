package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Customer;


public interface CustomerService {
  @Transactional
  Customer getCustomerByEmailAddress(String emailAddress);

    Page<Customer> findAll(Pageable pageable);
    Customer findById(Long customer_id);
}
