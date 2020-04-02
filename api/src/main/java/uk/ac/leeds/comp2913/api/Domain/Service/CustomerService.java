package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.transaction.annotation.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Customer;


public interface CustomerService {
  @Transactional
  Customer getCustomerByEmailAddress(String emailAddress);
}
