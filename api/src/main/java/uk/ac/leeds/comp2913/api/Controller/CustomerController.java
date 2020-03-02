package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import uk.ac.leeds.comp2913.api.Domain.Model.Customer;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @GetMapping("")
    public Customer customer(@RequestParam(value = "name", defaultValue = "World") String name) {
        Customer c = new Customer();
        c.setCreatedAt(new Date());
        return c;
    }
}
