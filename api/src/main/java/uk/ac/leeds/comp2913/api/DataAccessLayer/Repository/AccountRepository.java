package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
