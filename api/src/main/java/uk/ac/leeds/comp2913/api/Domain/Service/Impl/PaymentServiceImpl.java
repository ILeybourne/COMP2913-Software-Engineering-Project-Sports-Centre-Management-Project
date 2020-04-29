package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipService;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.PaymentService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

//TODO clean up
@Service
public class PaymentServiceImpl implements PaymentService {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final MembershipService membershipService;
    private final ActivityTypeService activityTypeService;
    private final MembershipTypeService membershipTypeService;
    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    public PaymentServiceImpl(CustomerService customerService, CustomerRepository customerRepository, AccountRepository accountRepository, @Lazy MembershipService membershipService, ActivityTypeService activityTypeService, MembershipTypeService membershipTypeService) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.membershipService = membershipService;
        this.activityTypeService = activityTypeService;
        this.membershipTypeService = membershipTypeService;
    }

    @Override
    public Boolean isStripeCustomer(Long customer_id) {
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        logger.info("/customer/stripe_id/customerid");
        if (customer_id != null) {
            if (customerRepository.findById(customer_id).isPresent()) {
                uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = customerRepository.findById(customer_id).get();
                if (internalCustomer.getStripeId() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public BigDecimal regularBookingPayment(BigDecimal originalAmount) {
        return originalAmount.multiply(BigDecimal.valueOf(0.7));
    }


    //Guest Checkout
    @Override
    public PayResponseBodyDTO create(String email, BigDecimal cost, Boolean regularSessionBooking) throws StripeException {
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info("request");
        logger.info("/guest-intent");
        Long newCost = ((cost.multiply(new BigDecimal(100.0))).longValue());
        Account account = new Account();
        try {
            //guest payment
            //new stripe customer
            CustomerCreateParams customerParams =
                    CustomerCreateParams.builder()
                            .setEmail(email)
                            .build();
            customer = Customer.create(customerParams);

            //new customer
            uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
            account.setCustomer(internalCustomer);
            internalCustomer.setEmailAddress(email);
            internalCustomer.setStripeId(customer.getId());
            //TODO get DOB
            internalCustomer.setDateOfBirth(new Date());
            customerRepository.save(internalCustomer);
            accountRepository.save(account);

            //Create Payment Intent
            PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                    .setCurrency("gbp")
                    .setAmount(newCost)
                    .setCustomer(customer.getId())
                    .putMetadata("integration_check", "accept_a_payment")
                    .build();
            intent = PaymentIntent.create(intentParams);

            logger.info("💰 PaymentIntent created!");
            responseBody.setClientSecret(intent.getClientSecret());
            responseBody.setAccountId(account.getId());
            responseBody.setAmountPaid(cost);
            responseBody.setTransactionId(intent.getId());
        } catch (CardException err) {
            // Handle "hard declines" e.g. insufficient funds, expired card, etc
            // See https://stripe.com/docs/declines/codes for more
            if (err.getCode().equals("authentication_required")) {
                responseBody.setError("This card requires authentication in order to proceeded. Please use a different card");
            } else {
                responseBody.setError(err.getMessage());
            }
        }
        return responseBody;
    }

    //Quick Payement (from those who have cards saved on stripe)
    @Override
    public PayResponseBodyDTO createFromSavedCard(Long customer_id, String email, BigDecimal inputCost, Boolean regularSessionBooking) throws StripeException {
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info("/intent/saved/customer id");
        Account account = null;
        BigDecimal cost = null;
        if (!regularSessionBooking) {
            cost = inputCost;
        } else if (regularSessionBooking) {
            cost = regularBookingPayment(inputCost);
        }
        Long newCost = (cost.multiply(new BigDecimal(100.0))).longValue();
        logger.info("new: " + cost.toString());
        if (membershipService.activeMemberCheck(email)) {
            account = membershipService.getMemberAccount(customer_id);
        } else if (!membershipService.activeMemberCheck(email)) {
            account = new Account();
        }
        try {
            if (customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id).get();
                //get stripe customer
                customer = Customer.retrieve(internalCustomer.getStripeId());
                //get list of payment methods
                PaymentMethodListParams params =
                        PaymentMethodListParams.builder()
                                .setCustomer(internalCustomer.getStripeId())
                                .setType(PaymentMethodListParams.Type.CARD)
                                .build();

                PaymentMethodCollection paymentMethods = PaymentMethod.list(params);

                //Get first card
                String paymentMethod = paymentMethods.getData().get(0).getId();

                PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                        .setCurrency("gbp")
                        .setAmount(newCost)
                        .setCustomer(internalCustomer.getStripeId())
                        .setPaymentMethod(paymentMethod)
                        .setConfirm(true)
                        .setOffSession(true)
                        // Verify your integration in this guide by including this parameter
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();

                intent = PaymentIntent.create(intentParams);
                logger.info("💰 Payment received!");
//                  The payment is complete and the money has been moved
//                  You can add any post-payment code here (e.g. shipping, fulfillment, etc)
                if (!membershipService.activeMemberCheck(email)) { //creating a new account if user is not a member
                    account.setCustomer(internalCustomer);
                    accountRepository.save(account);
                }
                responseBody.setClientSecret(intent.getClientSecret());
                responseBody.setTransactionId(intent.getId());
                responseBody.setAccountId(account.getId());
                responseBody.setAmountPaid(cost);
                Long account_id = account.getId();
                logger.info("account: " + account_id.toString());

            } else {
                responseBody.setError("Customer id not Found!");
            }
        } catch (CardException err) {
            // Handle "hard declines" e.g. insufficient funds, expired card, etc
            // See https://stripe.com/docs/declines/codes for more
            if (err.getCode().equals("authentication_required")) {
                responseBody.setError("This card requires authentication in order to proceeded. Please use a different card");
            } else {
                responseBody.setError(err.getMessage());
            }
        }
        return responseBody;
    }

    //Account user checkout
    @Override
    public PayResponseBodyDTO createFromNewCard(Long customer_id, String email, BigDecimal inputCost, Boolean regularSessionBooking) throws StripeException {
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        BigDecimal cost = null;
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = null;
        Customer customer = null;
        logger.info("/intent/card/customer id");
        Account account = null;
        if (!regularSessionBooking) {
            cost = inputCost;
        } else if (regularSessionBooking) {
            cost = regularBookingPayment(inputCost);
        }
        Long newCost = (cost.multiply(new BigDecimal(100.0))).longValue();
        logger.info("new: " + cost.toString());
        if (membershipService.activeMemberCheck(email)) {
            account = membershipService.getMemberAccount(customer_id);
        } else if (!membershipService.activeMemberCheck(email)) {
            account = new Account();
        }

        try {
            if (customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customer_id));

                //get stripe customer
                if (internalCustomer.getStripeId() == null) {
                    CustomerCreateParams customerParams =
                            CustomerCreateParams.builder()
                                    .setEmail(email)
                                    .build();
                    customer = Customer.create(customerParams);
                    internalCustomer.setStripeId(customer.getId());
                    customerRepository.save(internalCustomer);
                }

                if (!membershipService.activeMemberCheck(email)) { //creating a new account if user is not a member
                    account.setCustomer(internalCustomer);
                    accountRepository.save(account);
                }
                customer = Customer.retrieve(internalCustomer.getStripeId());
                logger.info(customer.getId(), customer.getAddress());

                // if (activity_id != null) {
                //get activity type
                //   Activity activity = activityService.findActivityById(activity_id);
                //guest payment

                //new stripe customer
//            CustomerCreateParams customerParams =
//                    CustomerCreateParams.builder()
//                            .setEmail(requestBody.getEmail())
//                            .build();
//
//            customer = Customer.create(customerParams);

                //new customer
//            uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
//            internalCustomer.setEmailAddress(requestBody.getEmail());
//            internalCustomer.setStripeId(customer.getId());
//            //TODO get DOB
//            internalCustomer.setDateOfBirth(new Date());
//            customerRepository.save(internalCustomer);

                //Create Payment Intent
                PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                        .setCurrency("gbp")
                        .setAmount(newCost)
                        .setCustomer(customer.getId())
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();
                intent = PaymentIntent.create(intentParams);

                logger.info("💰 PaymentIntent created!");
                responseBody.setClientSecret(intent.getClientSecret());
                responseBody.setTransactionId(intent.getId());
                responseBody.setAccountId(account.getId());
                responseBody.setAmountPaid(cost);

                //get last account and assign it to response
            }
        } catch (CardException err) {

            // Handle "hard declines" e.g. insufficient funds, expired card, etc
            // See https://stripe.com/docs/declines/codes for more
            if (err.getCode().equals("authentication_required")) {
                responseBody.setError("This card requires authentication in order to proceeded. Please use a different card");
            } else {
                responseBody.setError(err.getMessage());
            }
        }
        return responseBody;
    }

    public BigDecimal getActivityTypeCost(Long activityTypeId) {
        BigDecimal cost = activityTypeService.findById(activityTypeId).getCost();
        return cost;
    }

    public BigDecimal getMembershipTypeCost(Long membershipTypeId) {
        BigDecimal cost = membershipTypeService.findMembershipTypeById(membershipTypeId).getCost();
        return cost;
    }
}

