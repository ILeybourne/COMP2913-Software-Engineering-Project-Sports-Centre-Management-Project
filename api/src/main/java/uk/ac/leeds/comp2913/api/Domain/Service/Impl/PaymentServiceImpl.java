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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.ac.leeds.comp2913.api.Controller.PaymentController;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.Domain.Service.PaymentService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final ActivityService activityService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    public PaymentServiceImpl(ActivityService activityService, CustomerService customerService, CustomerRepository customerRepository){
        this.activityService = activityService;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @Override
    public Boolean isStripeCustomer(@PathVariable Long customer_id){
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        logger.info("/customer/stripe_id/customerid");
        if(customerRepository.findById(customer_id).isPresent()) {
            uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = customerRepository.findById(customer_id).get();
            if (internalCustomer.getStripeId() != null){
                return true;
            }
        }
        return false;
    }

    @Override
    public PayResponseBodyDTO createFromNewCard(Long customer_id, String email, Long activity_id) throws StripeException {
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = null;
        Customer customer = null;
        logger.info("/intent/card/customer id");
        try {
            if(customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customer_id));

                //get stripe customer
                if(internalCustomer.getStripeId() == null){
                    CustomerCreateParams customerParams =
                            CustomerCreateParams.builder()
                                    .setEmail(email)
                                    .build();
                    customer = Customer.create(customerParams);
                    internalCustomer.setStripeId(customer.getId());
                    customerRepository.save(internalCustomer);
                }
                customer = Customer.retrieve(internalCustomer.getStripeId());
                logger.info(customer.getId(), customer.getAddress());

                if (activity_id != null) {
                    //get activity type
                    Activity activity = activityService.findActivityById(activity_id);
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
                            .setAmount((activity.getCost().multiply(new BigDecimal(100.0))).longValue())
                            .setCustomer(customer.getId())
                            .putMetadata("integration_check", "accept_a_payment")
                            .build();
                    intent = PaymentIntent.create(intentParams);

                    logger.info("💰 PaymentIntent created!");
                    responseBody.setClientSecret(intent.getClientSecret());
                }}
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

    @Override
    public PayResponseBodyDTO create(String email, Long activity_id) throws StripeException{
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info( "request" );
        logger.info( activity_id.toString() );
        logger.info("/guest-intent");


        try {
            Activity activity = activityService.findActivityById(activity_id);
            //guest payment

            //new stripe customer
            CustomerCreateParams customerParams =
                    CustomerCreateParams.builder()
                            .setEmail(email)
                            .build();

            customer = Customer.create(customerParams);

            //new customer
            uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
            internalCustomer.setEmailAddress(email);
            internalCustomer.setStripeId(customer.getId());
            //TODO get DOB
            internalCustomer.setDateOfBirth(new Date());
            customerRepository.save(internalCustomer);

            //Create Payment Intent
            PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                    .setCurrency("gbp")
                    .setAmount((activity.getCost().multiply( new BigDecimal(100.0))).longValue())
                    .setCustomer(customer.getId())
                    .putMetadata("integration_check", "accept_a_payment")
                    .build();
            intent = PaymentIntent.create(intentParams);

            logger.info("💰 PaymentIntent created!");
            responseBody.setClientSecret(intent.getClientSecret());
        } catch (CardException err) {
            // Handle "hard declines" e.g. insufficient funds, expired card, etc
            // See https://stripe.com/docs/declines/codes for more
            if (err.getCode().equals("authentication_required")) {
                responseBody.setError("This card requires authentication in order to proceeded. Please use a different card");
            } else {
                responseBody.setError(err.getMessage());
            }
        }
        return  responseBody;
    }

    @Override
    public PayResponseBodyDTO createFromSavedCard(Long customer_id, String email, Long activity_id) throws StripeException{
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer =null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info("/intent/saved/customer id");

        try {
            if(customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id).get();
                //get stripe customer
                customer = Customer.retrieve(internalCustomer.getStripeId());

                if (activity_id != null) {
                    //get activity type
                    Activity activity = activityService.findActivityById(activity_id);

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
                            .setAmount((activity.getCost().multiply(new BigDecimal(100.0))).longValue())
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
                    responseBody.setClientSecret(intent.getClientSecret());
                }else{
                    responseBody.setError("Activity id not Found!");
                }
            }else {
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

    @Override
    public PayResponseBodyDTO addSubscriptionToCustomer(Long customer_id, Long activity_id) throws StripeException{
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer =null;
        Customer customer = null;
        PayResponseBodyDTO responseBody = new PayResponseBodyDTO();
        logger.info("/subscription/saved/customer_id");
        try {
            if(customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customer_id));

                //get stripe customer
                customer = Customer.retrieve(internalCustomer.getStripeId());

                Map<String, Object> item = new HashMap<>();
                item.put("plan", "plan_FSDjyHWis0QVwl");
                Map<String, Object> items = new HashMap<>();
                items.put("0", item);
                Map<String, Object> expand = new HashMap<>();
                expand.put("0", "latest_invoice.payment_intent");
                Map<String, Object> params = new HashMap<>();
                params.put("customer", customer.getId());
                params.put("items", items);
                params.put("expand", expand);
                Subscription subscription = Subscription.create(params);

                if (activity_id != null) {
                    //get activity type
                    Activity activity = activityService.findActivityById(activity_id);

                    //get list of payment methods
                    PaymentMethodListParams params2 =
                            PaymentMethodListParams.builder()
                                    .setCustomer(internalCustomer.getStripeId())
                                    .setType(PaymentMethodListParams.Type.CARD)
                                    .build();


                    PaymentMethodCollection paymentMethods = PaymentMethod.list(params2);

                    //Get first card
                    String paymentMethod = paymentMethods.getData().get(0).getId();

                    PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                            .setCurrency("gbp")
                            .setAmount((activity.getCost().multiply(new BigDecimal(100.0))).longValue())
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
                    responseBody.setClientSecret(intent.getClientSecret());
                }else{
                    responseBody.setError("Activity id not Found!");
                }
            }else {
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

}

