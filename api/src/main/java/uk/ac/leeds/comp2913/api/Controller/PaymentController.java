package uk.ac.leeds.comp2913.api.Controller;


import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.Domain.Service.PaymentService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityRequestBodyDTO;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //Guest Payment
    //TODO Move into response body
    @PostMapping(path = "/guest-intent/")
    public PayResponseBodyDTO create(@RequestBody ActivityRequestBodyDTO requestBody) throws StripeException {
        String emailAddress = requestBody.getEmail();
        Boolean repeating = requestBody.getRepeating();
        Long activityId= requestBody.getActivityId();
        Long membershipTypeId = requestBody.getMembershipId();
        return paymentService.create(emailAddress, activityId, repeating, membershipTypeId);
    }

    //Guest Payment
    //TODO Move into response body
    @PostMapping(path = "/intent/card/{customer_id}")
    public PayResponseBodyDTO createFromNewCard(@RequestBody ActivityRequestBodyDTO requestBody, @PathVariable Long customer_id) throws StripeException {
        String emailAddress = requestBody.getEmail();
        Long activityId= requestBody.getActivityId();
        Long membershipId = requestBody.getMembershipId();
        Boolean repeating = requestBody.getRepeating();
        return  paymentService.createFromNewCard(customer_id, emailAddress, activityId, repeating, membershipId);
    }

    //Customer Saved Card Payment
    @PreAuthorize("hasAuthority('SCOPE_can:cash_booking')")
    @PostMapping(path = "/intent/saved/{customer_id}")
    public PayResponseBodyDTO createFromSavedCard(@RequestBody ActivityRequestBodyDTO requestBody, @PathVariable Long customer_id) throws StripeException {
        String emailAddress = requestBody.getEmail();
        Long activityId = requestBody.getActivityId();
        Long membershipId = requestBody.getMembershipId();
        Boolean repeating = requestBody.getRepeating();
        return paymentService.createFromSavedCard(customer_id, emailAddress, activityId, repeating, membershipId);
    }

    //Customer Saved Card Payment
    @PreAuthorize("hasAuthority('SCOPE_can:cash_booking')")
    @PostMapping(path = "/subscription/saved/{customer_id}")
    public PayResponseBodyDTO addSubscriptionToCustomer(@RequestBody ActivityRequestBodyDTO requestBody, @PathVariable Long customer_id) throws StripeException {
        Long activityId= requestBody.getActivityId();
        return paymentService.addSubscriptionToCustomer(customer_id, activityId);
    }

    @PostMapping(path = "/customer/stripe_id/{customer_id}")
    public Boolean isStripeCustomer(@PathVariable Long customer_id){
        return paymentService.isStripeCustomer(customer_id);
    }
}
