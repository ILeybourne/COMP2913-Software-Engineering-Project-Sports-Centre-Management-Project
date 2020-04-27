package uk.ac.leeds.comp2913.api.Domain.Service;

import com.stripe.exception.StripeException;

import org.springframework.web.bind.annotation.PathVariable;

import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

public interface PaymentService {
    Boolean isStripeCustomer(Long customer_id);
    PayResponseBodyDTO createFromNewCard(Long customer_id, String email, Long activity_id, Boolean repeating, Long membershipTypeId) throws StripeException;
    PayResponseBodyDTO create(String email, Long activity_id, Boolean repeating, Long membershipTypeId) throws StripeException;
    PayResponseBodyDTO createFromSavedCard(Long customer_id, String email, Long activity_id, Boolean repeating, Long membershipTypeId) throws StripeException;
    PayResponseBodyDTO addSubscriptionToCustomer(Long customer_id, Long activity_id) throws StripeException;
}
