package uk.ac.leeds.comp2913.api.Domain.Service;

import com.stripe.exception.StripeException;

import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import uk.ac.leeds.comp2913.api.ViewModel.PayResponseBodyDTO;

public interface PaymentService {
    Boolean isStripeCustomer(Long customer_id);
    PayResponseBodyDTO createFromNewCard(Long customer_id, String email, BigDecimal cost, Boolean regularSessionBooking, Integer participants) throws StripeException;
    PayResponseBodyDTO create(String email, BigDecimal cost, Boolean regularSessionBooking, Integer Participants) throws StripeException;
    PayResponseBodyDTO createFromSavedCard(Long customer_id, String email, BigDecimal cost, Boolean regularSessionBooking, Integer Participants) throws StripeException;
    BigDecimal getBookingCharge(Long activityId);
    BigDecimal getMembershipTypeCost(Long membershipTypeId);
    PayResponseBodyDTO cashPayment(String email, BigDecimal inputCost,Integer participants);
}
