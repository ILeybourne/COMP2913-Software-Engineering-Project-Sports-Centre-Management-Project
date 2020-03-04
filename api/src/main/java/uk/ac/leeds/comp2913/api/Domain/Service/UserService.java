package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.transaction.annotation.Transactional;
import uk.ac.leeds.comp2913.api.Domain.Model.User;


public interface UserService {
  @Transactional
  User getUserById(long id);

  @Transactional
  User getUserByEmailAddress(String emailAddress);

  @Transactional
  User save(User user);
}
