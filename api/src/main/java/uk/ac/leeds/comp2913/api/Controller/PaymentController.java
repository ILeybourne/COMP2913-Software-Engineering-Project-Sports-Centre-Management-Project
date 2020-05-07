package uk.ac.leeds.comp2913.api.Controller;


import com.stripe.exception.StripeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import uk.ac.leeds.comp2913.api.Domain.Service.PaymentService;
import uk.ac.leeds.comp2913.api.ViewModel.PaymentDTO;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //Guest Payment
    //TODO Move into response body
    @PostMapping(path = "/guest-intent/")
    public PayResponseBodyDTO create(@RequestBody PaymentDTO requestBody) throws StripeException {
        String emailAddress = requestBody.getEmail();
        Integer participants = 0;
        BigDecimal cost = null;
        if (requestBody.getSessionId() != null) {
            cost = paymentService.getBookingCharge(requestBody.getSessionId());
            participants = requestBody.getParticipants();
        }
        if (requestBody.getMembershipTypeId() != null) {
            cost = paymentService.getMembershipTypeCost(requestBody.getMembershipTypeId());
        }
        if (participants == null) {
            participants = 0;
        }
        return paymentService.create(emailAddress, cost, false, participants);
    }

    //Customer New Card Payment
    //TODO Move into response body
    @PostMapping(path = "/intent/card/{customer_id}")
    public PayResponseBodyDTO createFromNewCard(@RequestBody PaymentDTO requestBody, @PathVariable Long customer_id) throws StripeException {
        String emailAddress = requestBody.getEmail();
        Integer participants = 0;
        BigDecimal cost = null;
        if (requestBody.getSessionId() != null) {
            cost = paymentService.getBookingCharge(requestBody.getSessionId());
            participants = requestBody.getParticipants();
        }
        if (requestBody.getMembershipTypeId() != null) {
            cost = paymentService.getMembershipTypeCost(requestBody.getMembershipTypeId());
        }
        Boolean regularSessionBooking = requestBody.getRegularSession();
        if (regularSessionBooking == null) {
            regularSessionBooking = false;
        }
        if (participants == null) {
            participants = 0;
        }
        return paymentService.createFromNewCard(customer_id, emailAddress, cost, regularSessionBooking, participants);
    }

    //Customer Saved Card Payment
    @PostMapping(path = "/intent/saved/{customer_id}")
    public PayResponseBodyDTO createFromSavedCard(@RequestBody PaymentDTO requestBody, @PathVariable Long customer_id) throws StripeException {
        String emailAddress = requestBody.getEmail();
        Integer participants = 0;
        BigDecimal cost = null;
        if (requestBody.getSessionId() != null) {
            cost = paymentService.getBookingCharge(requestBody.getSessionId());
            participants = requestBody.getParticipants();
        }
        if (requestBody.getMembershipTypeId() != null) {
            cost = paymentService.getMembershipTypeCost(requestBody.getMembershipTypeId());
        }
        Boolean regularSessionBooking = requestBody.getRegularSession();
        if (regularSessionBooking == null) {
            regularSessionBooking = false;
        }
        if (participants == null) {
            participants = 0;
        }
        return paymentService.createFromSavedCard(customer_id, emailAddress, cost, regularSessionBooking, participants);
    }

    @PostMapping(path = "/customer/stripe_id/{customer_id}")
    public Boolean isStripeCustomer(@PathVariable Long customer_id) {
        return paymentService.isStripeCustomer(customer_id);
    }

    //Cash end point for walk in customers (employee feature)
    @PostMapping(path = "/cash")
    public PayResponseBodyDTO cashPayment(@RequestBody PaymentDTO requestBody) {
        String emailAddress = requestBody.getEmail();
        Integer participants = 0;
        BigDecimal cost = null;
        if (requestBody.getSessionId() != null) {
            cost = paymentService.getBookingCharge(requestBody.getSessionId());
            participants = requestBody.getParticipants();
        }
        return paymentService.cashPayment(emailAddress, cost, participants);
    }
}
