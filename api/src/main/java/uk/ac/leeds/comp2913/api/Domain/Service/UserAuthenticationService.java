package uk.ac.leeds.comp2913.api.Domain.Service;

import uk.ac.leeds.comp2913.api.Domain.Model.User;

import java.util.Optional;

public interface UserAuthenticationService {
  Optional<String> login(String emailAddress, String password);

  Optional<User> findByToken(String token);

  void logout(User user);
}
