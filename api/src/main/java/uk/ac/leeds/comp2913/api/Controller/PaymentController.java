package uk.ac.leeds.comp2913.api.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.PaymentIntentRepository;




@RestController
@RequestMapping("/payments")
public class PaymentController {
    private static Gson gson = new Gson();

    static class StripeKeyResponse {
        private String publishableKey;

        public StripeKeyResponse(String publishableKey) {
            this.publishableKey = publishableKey;
        }
    }

    static class PayRequest {
        @SerializedName("items")
        Object[] items;
        @SerializedName("paymentMethodId")
        String paymentMethodId;
        @SerializedName("currency")
        String currency;

        public Object[] getItems() {
            return items;
        }

        public String getPaymentMethodId() {
            return paymentMethodId;
        }

        public String getCurrency() {
            return currency;
        }

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
    }

    static int calculateOrderAmount(Object[] items) {
        // Replace this constant with a calculation of the order's amount

        // Calculate the order total on the server to prevent
        // users from directly manipulating the amount on the client
        return 1000;
    }

    @GetMapping(path = "/stripe-key")
    public String getKey(@RequestBody JsonObject json) {
            // Send publishable key to client
            return gson.toJson(new StripeKeyResponse("pk_test_crv9Zb7tvQtSJ82FhQwrnb8k00v3eIOvj8"));
    }

    @PostMapping(path = "/intent", consumes = "application/json", produces = "application/json")
    // from https://blog.hackages.io/create-a-simple-payment-flow-with-stripe-b1d0f0f94337
    public String create(@RequestBody JsonObject json) throws StripeException {
        PayRequest confirmRequest = gson.fromJson(json, PayRequest.class);

        PaymentIntent intent = null;
        PayResponseBody responseBody = new PayResponseBody();
        try {
            int orderAmount = calculateOrderAmount(confirmRequest.getItems());
            // Create new PaymentIntent with a PaymentMethod ID from the client.
            PaymentIntentCreateParams.Builder createParamsBuilder = new PaymentIntentCreateParams.Builder()
                    .setCurrency(confirmRequest.getCurrency()).setAmount((long) orderAmount)
                    .setPaymentMethod(confirmRequest.getPaymentMethodId())
                    .setErrorOnRequiresAction(true).setConfirm(true);
            PaymentIntentCreateParams createParams = createParamsBuilder.build();
            intent = PaymentIntent.create(createParams);

            System.out.println("💰 Payment received!");
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

        return gson.toJson(responseBody);
    }
}
