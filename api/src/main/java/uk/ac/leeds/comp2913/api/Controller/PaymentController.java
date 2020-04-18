package uk.ac.leeds.comp2913.api.Controller;


import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomerRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityTypeService;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final ActivityTypeService activityTypeService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    public PaymentController(ActivityTypeService activityTypeService, CustomerService customerService, CustomerRepository customerRepository) {
        this.activityTypeService = activityTypeService;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    static class PayResponseBody {
        private String clientSecret;
        private Boolean requiresAction;
        private String error;

        public PayResponseBody() {

        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public void setRequiresAction(Boolean requiresAction) {
            this.requiresAction = requiresAction;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public Boolean getRequiresAction() {
            return requiresAction;
        }

        public String getError() {
            return error;
        }
    }

    static class  ActivityRequestBody {
        private Long activityTypeId;
        private Boolean regularSession;
        private String email;
        private Long customerId;


        public Long getActivityTypeId() {
            return activityTypeId;
        }

        public void setActivityTypeId(Long activityTypeId) {
            this.activityTypeId = activityTypeId;
        }

        public Boolean getRegularSession() {
            return regularSession;
        }

        public void setRegularSession(Boolean regularSession) {
            this.regularSession = regularSession;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }
    }

    //Guest Payment
    //TODO Move into response body
    @PostMapping(path = "/intent/")
    // from https://blog.hackages.io/create-a-simple-payment-flow-with-stripe-b1d0f0f94337
    public PayResponseBody create(@RequestBody ActivityRequestBody requestBody) throws StripeException {
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        Customer customer = null;
        PayResponseBody responseBody = new PayResponseBody();
        logger.info( "request" );
        logger.info( requestBody.getActivityTypeId().toString() );

        try {
            ActivityType activityType = activityTypeService.findById(requestBody.getActivityTypeId().longValue());
            //guest payment

            //new stripe customer
            CustomerCreateParams customerParams =
                    CustomerCreateParams.builder()
                            .setEmail(requestBody.getEmail())
                            .build();

            customer = Customer.create(customerParams);

            //new customer
            uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
            internalCustomer.setEmailAddress(requestBody.getEmail());
            internalCustomer.setStripeId(customer.getId());
            //TODO get DOB
            internalCustomer.setDateOfBirth(new Date());
            customerRepository.save(internalCustomer);

            //Create Payment Intent
            PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                            .setCurrency("gbp")
                            .setAmount((activityType.getCost().multiply( new BigDecimal(100.0))).longValue())
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


    //TODO Move into response body
    @PostMapping(path = "/intent/{customer_id}")
    // from https://blog.hackages.io/create-a-simple-payment-flow-with-stripe-b1d0f0f94337
    public PayResponseBody create(@RequestBody ActivityRequestBody requestBody, @PathVariable Long customer_id) throws StripeException {
        //TODO Move to env
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        uk.ac.leeds.comp2913.api.Domain.Model.Customer internalCustomer =null;
        Customer customer = null;
        PayResponseBody responseBody = new PayResponseBody();
        try {
            if(customerRepository.findById(customer_id).isPresent()) {
                //get api customer
                internalCustomer = customerRepository.findById(customer_id).get();
                //get stripe customer
                customer = Customer.retrieve(internalCustomer.getStripeId());

                if (requestBody.getActivityTypeId() != null) {
                    //get activity type
                    ActivityType activityType = activityTypeService.findById(requestBody.getActivityTypeId());


                    //get list of payment methods
                    PaymentMethodListParams params =
                            PaymentMethodListParams.builder()
                                    .setCustomer(internalCustomer.getStripeId())
                                    .setType(PaymentMethodListParams.Type.CARD)
                                    .build();


                    //TODO set customer on sign-up of user
                    //Search for customer by ID
//            if (customerService.findById(customer_id) != null) {
////                Customer customer = customerService.findById(customer_id);
//            }else{
                    //Else create new customer
                    //TODO Create on guest payment

                    //new customer
//                CustomerCreateParams customerParams =
//                        CustomerCreateParams.builder()
//                                .setEmail("email@email.com")
//                                .build();
//
//                customer = Customer.create(customerParams);

//            Card card = (Card) customer.getSources().getData();


//
                    PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
                    logger.info("paymentMethods.getData().get(0).getId()");
//            logger.info(paymentMethods.getData().get(0).getId());
//                uk.ac.leeds.comp2913.api.Domain.Model.Customer serverCustomer = new uk.ac.leeds.comp2913.api.Domain.Model.Customer();
//                serverCustomer.setId(1L);
//                serverCustomer.setEmailAddress(customer.getId());
//                customerRepository.save(serverCustomer);
//            }

//            return paymentMethods;

//
                    PaymentIntentCreateParams intentParams = PaymentIntentCreateParams.builder()
                            .setCurrency("gbp")
                            .setAmount((activityType.getCost().multiply(new BigDecimal(100.0))).longValue())
                            .setCustomer("cus_H62V7iQcZ1just NTyE")
                            .setPaymentMethod(paymentMethods.getData().get(0).getId())
                            .setConfirm(true)
                            .setOffSession(true)
                            // Verify your integration in this guide by including this parameter
                            .putMetadata("integration_check", "accept_a_payment")
                            .build();
                    intent = PaymentIntent.create(intentParams);
//
//            logger.info("💰 Payment received!");
//            // The payment is complete and the money has been moved
//            // You can add any post-payment code here (e.g. shipping, fulfillment, etc)
//
                    responseBody.setClientSecret(intent.getClientSecret());
                }
            }
            responseBody.setError("Customer id not Found!");
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
//        return null;
    }
}
