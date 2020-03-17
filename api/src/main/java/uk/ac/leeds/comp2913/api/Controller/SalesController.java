package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.PaymentRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Payment;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@RestController
@RequestMapping("")
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final MembershipRepository membershipRepository;
    private final BookingRepository bookingRepository;


    @Autowired
    public PaymentController(PaymentRepository paymentRepository, MembershipRepository membershipRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.membershipRepository = membershipRepository;
        this.bookingRepository = bookingRepository;
    }

    // get all payments
    @GetMapping("/payments")
    public Page<Payment> getPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    //Pay for a membership. Post membership to payment database
    @PostMapping("/membership/{membership_id}/checkout")
    public Payment addMembershipPayment(@PathVariable Long membership_id,
                                    @Valid @RequestBody Payment payment){
        return membershipRepository.findById(membership_id)
                .map(membership -> {
                    payment.setSale(membership);
                    payment.setSalePrice(membership.getCost());
                    return paymentRepository.save(payment);
                }) .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id " + membership_id));
    }

    //Pay for a booking. Post booking to payment database
    @PostMapping("payments/booking/{booking_id}")
    public Payment addBookingPayment(@PathVariable Long booking_id,
                                        @Valid @RequestBody Payment payment){
        return bookingRepository.findById(booking_id)
                .map(booking -> {
                    payment.setSale(booking);
                    payment.setSalePrice(booking.getCost());
                    return paymentRepository.save(payment);
                }) .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + booking_id));
    }

    // Update Payment
    @PutMapping("/{payment_id}/revise")
    public Payment updatePayment(@PathVariable Long payment_id,
                                 @Valid @RequestBody Payment paymentRequest) {
        return paymentRepository.findById(payment_id)
                .map(payment -> {
                    payment.setSalePrice(paymentRequest.getSalePrice());
                    return paymentRepository.save(payment);
                }).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + payment_id));
    }

    //cancel / refund payment
    @DeleteMapping("/refund/{payment_id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long payment_id) {
        return paymentRepository.findById(payment_id)
                .map(payment -> {
                    paymentRepository.delete(payment);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("payment not found with id" + payment_id));
    }
}
