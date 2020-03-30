package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import java.math.BigDecimal;
import java.util.List;

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
            this.customerRepository.save(c);
        }


        Booking s1 = new Booking();
        s1.setAmount(BigDecimal.valueOf(220));
        Booking s2 = new Booking();
        s2.setAmount(BigDecimal.valueOf(220));
        Membership s3 = new Membership();
        s3.setAmount(BigDecimal.valueOf(220));
        Booking s4 = new Booking();
        s4.setAmount(BigDecimal.valueOf(220));
        sales = List.of(s1, s2, s3, s4);
    }

    @AfterEach
    void tearDown() {
        this.customerRepository.delete(c);
    }

    @Test
    void invoice() {
        Receipt r = receiptService.invoice("12345678", sales, c);
        receiptService.delete(r.getId());
    }
}
