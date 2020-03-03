package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.leeds.comp2913.api.Domain.Model.User;

@RestController
@RequestMapping("/users")
final class UserProfileController {

  @GetMapping("/current")
  String getCurrent(@AuthenticationPrincipal final Jwt user) {
//    TODO: return an Object
//    TODO: Jackson
//    TODO: find by Username
//    TODO: Use permissions from token
//    TODO: Only enable JwtAuth in Security
    return user.toString();
  }

}
