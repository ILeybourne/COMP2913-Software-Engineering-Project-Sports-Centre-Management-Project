package uk.ac.leeds.comp2913.api.Domain.Service;

import uk.ac.leeds.comp2913.api.Domain.Model.User;

public interface UserService {
  User getUserById(long id);

  User getUserByEmailAddress(String emailAddress);

  User save(User user);
}
