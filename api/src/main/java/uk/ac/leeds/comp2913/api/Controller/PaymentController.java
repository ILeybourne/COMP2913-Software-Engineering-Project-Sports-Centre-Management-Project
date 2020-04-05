package uk.ac.leeds.comp2913.api.Controller;


import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



//import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.PaymentIntentRepository;




@RestController
@RequestMapping("/payments")
public class PaymentController {

    Logger logger = LoggerFactory.getLogger(PaymentController.class);


//    static class StripeKeyResponse {
//        public String getPublishableKey() {
//            return publishableKey;
//        }
//
//        public void setPublishableKey(String publishableKey) {
//            this.publishableKey = publishableKey;
//        }
//
//        private String publishableKey;
//
//        public StripeKeyResponse(String publishableKey) {
//            this.publishableKey = publishableKey;
//        }
//    }

//    static class PayRequest {
//        @SerializedName("items")
//        Object[] items;
//        @SerializedName("paymentMethodId")
//        String paymentMethodId;
//        @SerializedName("currency")
//        String currency;
//
//        public Object[] getItems() {
//            return items;
//        }
//
//        public String getPaymentMethodId() {
//            return paymentMethodId;
//        }
//
//        public String getCurrency() {
//            return currency;
//        }
//
//    }

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

    static int calculateOrderAmount(Object[] items) {
        // Replace this constant with a calculation of the order's amount

        // Calculate the order total on the server to prevent
        // users from directly manipulating the amount on the client
        return 1000;
    }

//    @GetMapping(path = "/stripe-key")
//    public StripeKeyResponse getKey() {
//            // Send publishable key to client
//            return (new StripeKeyResponse("pk_test_crv9Zb7tvQtSJ82FhQwrnb8k00v3eIOvj8"));
//    }

    @PostMapping(path = "/intent")
    // from https://blog.hackages.io/create-a-simple-payment-flow-with-stripe-b1d0f0f94337
    public PayResponseBody create() throws StripeException {
        Stripe.apiKey = "sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX";
        PaymentIntent intent = null;
        PayResponseBody responseBody = new PayResponseBody();
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                            .setCurrency("gbp")
                            .setAmount(1099L)
                            // Verify your integration in this guide by including this parameter
                            .putMetadata("integration_check", "accept_a_payment")
                            .build();
            intent = PaymentIntent.create(params);

            logger.info("💰 Payment received!");
            // The payment is complete and the money has been moved
            // You can add any post-payment code here (e.g. shipping, fulfillment, etc)

            // Send the client secret to the client to use in the demo
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
}
