package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;

import java.util.Date;

@RestController
@RequestMapping("/membership")
public class MembershipController {
    @GetMapping("")
    public Membership membership(@RequestParam(value = "name", defaultValue = "World") String name) {
        Membership m = new Membership();
        m.setCreatedAt(new Date());
        return m;
    }
}
