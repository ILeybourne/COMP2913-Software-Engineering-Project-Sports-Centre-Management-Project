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
import java.util.UUID;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.AccountRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
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
    private final ActivityService activityService;
    private final MembershipTypeService membershipTypeService;
    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    public PaymentServiceImpl(CustomerService customerService, CustomerRepository customerRepository, AccountRepository accountRepository, @Lazy MembershipService membershipService, ActivityService activityService, MembershipTypeService membershipTypeService) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.membershipService = membershipService;
        this.activityService = activityService;
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
    public BigDecimal memberBookingDiscount(BigDecimal originalAmount) {
        return originalAmount.multiply(BigDecimal.valueOf(0.7));
    }
    public BigDecimal calculateBookingTotal(BigDecimal originalAmount, Boolean regularBooking, Integer participants, Boolean member){
        logger.info("calculate booking");
        BigDecimal total = null;
        if(member){
            //If a member then apply the discount to 1xcost
            BigDecimal memberCost = memberBookingDiscount(originalAmount);
            //If the member is booking an activity with 1+ participants (incl themselves), charge the remaining participants the full cost
            if(participants >= 1 && !regularBooking){
                BigDecimal participantsTotal = originalAmount.multiply(BigDecimal.valueOf((participants-1))); //if there is just 1 participant(member) then this will be 0
                total = memberCost.add(participantsTotal); //calculate total

            }else if(regularBooking){
                //regular session subscribers can only have 1 participant, so take the member cost and apply the further discount
                total = regularBookingPayment(memberCost);
            }
        }
        else {
            //if not a member and not booking on to a regular session, then charge all participants full price
            if(!regularBooking){
                total = originalAmount.multiply(BigDecimal.valueOf(participants));
            } else {
                //otherwise if it is a regular session subscription, apply the discount
                total = regularBookingPayment(originalAmount);
            }
        }
        return total;
    }

    //Guest Checkout
    @Override
    public PayResponseBodyDTO create(String email, BigDecimal cost, Boolean regularSessionBooking, Integer participants) throws StripeException {
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info("request");
        logger.info("/guest-intent");
        BigDecimal salesCost = cost; //default sales cost from input
        //If participants is greater than 0 then this is a booking payment, otherwise, if 0 then its a membership payment
        if(participants != 0){
            salesCost = calculateBookingTotal(cost, regularSessionBooking, participants, false); //guests are obviously not members so defaulted to false
        }
        Long newCost = ((salesCost.multiply(new BigDecimal(100.0))).longValue());
        //create a new account for the guest checking out
        Account account = new Account();
        try {
            uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = customerRepository.findByEmailAddress(email);
            if (internalCustomer == null) {
                //If customer does not already exist create new API Customer and Stripe Customer
                internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
                CustomerCreateParams customerParams =
                        CustomerCreateParams.builder()
                                .setEmail(email)
                                .build();
                customer = Customer.create(customerParams);
            } else {
                if(internalCustomer.getStripeId()!= null) {
                    customer = Customer.retrieve(internalCustomer.getStripeId());
                }else{
                    CustomerCreateParams customerParams =
                            CustomerCreateParams.builder()
                                    .setEmail(email)
                                    .build();
                    customer = Customer.create(customerParams);
                }
            }
            //Assign account and email to new internal customer
            account.setCustomer(internalCustomer);
            internalCustomer.setEmailAddress(email);
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

            //Generate the response, this data is used to post the booking from the front end
            responseBody.setClientSecret(intent.getClientSecret());
            responseBody.setAccountId(account.getId());
            responseBody.setAmountPaid(new BigDecimal(newCost.toString()));
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
    public PayResponseBodyDTO createFromSavedCard(Long customer_id, String email, BigDecimal inputCost, Boolean regularSessionBooking, Integer participants) throws StripeException {
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info("/intent/saved/customer id");
        Account account = null;
        Boolean member = false;
        //default sales cost to input cost
        BigDecimal salesCost = inputCost;
        //Check to see whether it is a member checking out, if so, use their account to record the sale
        if (membershipService.activeMemberCheck(email)) {
            account = membershipService.getMemberAccount(customer_id);
            member = membershipService.activeMemberCheck(email);
        } else if (!membershipService.activeMemberCheck(email)) {
            account = new Account();
        }
        //If participants is greater than 0 then this is a booking payment, otherwise, if 0 then its a membership payment
        if(participants != 0){
            salesCost = calculateBookingTotal(inputCost, regularSessionBooking, participants, member);
        }
        Long newCost = (salesCost.multiply(new BigDecimal(100.0))).longValue();
        logger.info("new: " + newCost.toString());
        try {
            if (customerRepository.findById(customer_id).isPresent()) {
                //Get API Customer
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
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();

                intent = PaymentIntent.create(intentParams);
                logger.info("💰 Payment received!");

//                  The payment is complete and the money has been moved
//                  You can add any post-payment code here (e.g. shipping, fulfillment, etc)
                if (!member) { //assigning the new account if user is not a member
                    account.setCustomer(internalCustomer);
                    accountRepository.save(account);
                }
                responseBody.setClientSecret(intent.getClientSecret());
                responseBody.setTransactionId(intent.getId());
                responseBody.setAccountId(account.getId());
                responseBody.setAmountPaid(new BigDecimal(newCost.toString()));
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
    public PayResponseBodyDTO createFromNewCard(Long customer_id, String email, BigDecimal inputCost, Boolean regularSessionBooking, Integer participants) throws StripeException {
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        BigDecimal cost = null;
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = null;
        Customer customer = null;
        logger.info("/intent/card/customer id");
        Account account = null;
        Boolean member = false;
        BigDecimal salesCost = inputCost;
        if (membershipService.activeMemberCheck(email)) {
            account = membershipService.getMemberAccount(customer_id);
            member = membershipService.activeMemberCheck(email);
        } else if (!membershipService.activeMemberCheck(email)) {
            account = new Account();
        }
        if(participants != 0){
            salesCost = calculateBookingTotal(inputCost, regularSessionBooking, participants, member);
        }
        Long newCost = (salesCost.multiply(new BigDecimal(100.0))).longValue();
        logger.info("new: " + newCost.toString());
        try {
            internalCustomer = customerRepository.findByEmailAddress(email);
            if (internalCustomer == null) {
                //If customer does not already exist create new API Customer and Stripe Customer
                internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
                internalCustomer.setEmailAddress(email);
                internalCustomer.setDateOfBirth(new Date());
                customerRepository.save(internalCustomer);
                customer_id = internalCustomer.getId();
                logger.info("asdsasas");
                logger.info(customer_id.toString());
            }

            if (customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id "));

                //get stripe customer
                logger.info("get stripe Customer");
                logger.info(internalCustomer.getStripeId());
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

                //Create Payment Intent
                PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                        .setCurrency("gbp")
                        .setAmount(newCost)
                        .setCustomer(customer.getId())
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();
                intent = PaymentIntent.create(intentParams);

                logger.info("💰 PaymentIntent created!");

                //Payment response, contains data required for posting membership/booking record
                responseBody.setClientSecret(intent.getClientSecret());
                responseBody.setTransactionId(intent.getId());
                responseBody.setAccountId(account.getId()); //this gets the account record
                responseBody.setAmountPaid(new BigDecimal(newCost.toString()));

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

    public BigDecimal getBookingCharge(Long activity_id) {
        BigDecimal cost = activityService.findActivityById(activity_id).getCost();
        return cost;
    }

    public BigDecimal getMembershipTypeCost(Long membershipTypeId) {
        BigDecimal cost = membershipTypeService.findMembershipTypeById(membershipTypeId).getCost();
        return cost;
    }

    public PayResponseBodyDTO cashPayment(String email, BigDecimal inputCost,Integer participants) {
        BigDecimal salesCost = inputCost;
        Account account = null;
        Boolean member = false;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = null;
        if(customerRepository.findByEmailAddress(email) == null) {
            internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
            account = new Account();
            internalCustomer.setEmailAddress(email);
            internalCustomer.setDateOfBirth(new Date());
            account.setCustomer(internalCustomer);
            customerRepository.save(internalCustomer);
            accountRepository.save(account);
        } else{
            internalCustomer = customerRepository.findByEmailAddress(email);
            if (membershipService.activeMemberCheck(email)) {
                account = membershipService.getMemberAccount(internalCustomer.getId());
                member = membershipService.activeMemberCheck(email);
            } else {
                account = new Account();
                account.setCustomer(internalCustomer);
                accountRepository.save(account);
            }
        }
        salesCost = calculateBookingTotal(inputCost, false, participants, member);
        String transactionId = "CASH" + UUID.randomUUID().toString();
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        //Payment response, contains data required for posting booking record
        responseBody.setTransactionId("Cash");
        responseBody.setAccountId(account.getId()); //this gets the account record
        responseBody.setAmountPaid(salesCost);
        return responseBody;
    }
}

