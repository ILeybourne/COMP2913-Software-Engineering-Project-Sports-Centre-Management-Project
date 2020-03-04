package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import uk.ac.leeds.comp2913.api.Domain.Model.Account;

@RestController
@RequestMapping("/account")
public class AccountController {
    @GetMapping("")
    public Account account(@RequestParam(value = "name", defaultValue = "World") String name) {
        Account a = new Account();
        a.setCreatedAt(new Date());
        return a;
    }
}
