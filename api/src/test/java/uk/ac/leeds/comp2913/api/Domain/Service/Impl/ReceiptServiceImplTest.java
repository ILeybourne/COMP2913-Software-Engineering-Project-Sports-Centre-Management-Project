package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReceiptServiceImplTest {

    ReceiptService receiptService;
    private List<Sale> sales;
    private Customer c;
    private CustomerRepository customerRepository;

    @Autowired
    ReceiptServiceImplTest(CustomerRepository customerRepository, ReceiptService receiptService) {
        this.customerRepository = customerRepository;
        this.receiptService = receiptService;
    }

    @BeforeEach
    void setUp() {
        final String emailAddress = "sebastiangarwood@outlook.com";

        c = customerRepository.findByEmailAddress(emailAddress);

        if (c == null) {
            c = new Customer();
            c.setEmailAddress(emailAddress);
            c.setDateOfBirth(new Date());
            this.customerRepository.save(c);
        }


        Booking s1 = new Booking();
        s1.setAmount(BigDecimal.valueOf(220));
        final Activity activity = new Activity();
        s1.setActivity(activity);
        Booking s2 = new Booking();
        s2.setAmount(BigDecimal.valueOf(220));
        Membership s3 = new Membership();
        s3.setAmount(BigDecimal.valueOf(220));
        Booking s4 = new Booking();
        s4.setAmount(BigDecimal.valueOf(220));
        sales = List.of(s1);
    }

    @AfterEach
    void tearDown() {
        this.customerRepository.delete(c);
    }

    /**
     * <p>
     * Issues with cascading when deleting and persisting to storage
     * <p>
     * Example how to test service layer, issue with
     */
    @Disabled
    @Test
    void invoice() throws IOException, MessagingException {
        Receipt r = receiptService.invoice("12345678", sales, c);
        receiptService.delete(r.getId());
    }
}
