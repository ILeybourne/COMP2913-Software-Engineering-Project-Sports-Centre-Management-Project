package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;

@RestController
public class BookingController {


  @GetMapping("/booking")
  public Booking booking(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new Booking();
  }
}
