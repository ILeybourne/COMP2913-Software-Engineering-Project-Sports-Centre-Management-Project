package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

import java.util.Date;

@Entity
public class Booking extends Sale {

    @CreationTimestamp
    private Date created_at;

    @JsonIgnore
    @UpdateTimestamp
    private Date updated_at;

    @Range(min = 1)
    private int participants;

    /**
     * The account associated with the booking
     */
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    /**
     * The Activity associated with the booking
     */
    @NotNull(message = "Activity is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "regular_session_id", nullable = true)
    private RegularSession regularSession;

    public Booking() {
    }

    public static Booking createBookingFromRegularSession(Activity activity, Booking booking) {
        Booking b = new Booking();
        b.setActivity(activity);
        b.setRegularSession(booking.getRegularSession());
        b.setParticipants(booking.getParticipants());
        b.setAccount(booking.getAccount());
        b.amount = booking.getAmount();
        return b;
    }

    //Payments are taken prior to the creation of sale objects, so this is calculated in the payment service
    // @Override
    // public void setAmount(BigDecimal amount) {
    //     if (regularSession != null) {
    //         this.amount = (calculateRegularSessionAmount(amount));
    //     } else {
    //         this.amount = (amount);
    //     }
    // }

    // public BigDecimal calculateRegularSessionAmount(BigDecimal originalAmount) {
    //     return originalAmount.multiply(BigDecimal.valueOf(0.7));
    // }

    @JsonIgnore
    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    @JsonIgnore
    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @JsonIgnore
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return "Booking on " + getCreatedAt();
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    @JsonIgnore
    public RegularSession getRegularSession() {
        return regularSession;
    }

    public void setRegularSession(RegularSession regularSession) {
        this.regularSession = regularSession;
    }
}
