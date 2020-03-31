package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Booking extends Sale {

  @CreationTimestamp
  private Date created_at;
  @UpdateTimestamp
  private Date updated_at;

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
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "activity_id")
  private Activity activity;


  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "regular_session_id", nullable = true)
  private RegularSession  regularSession;


  public Booking() {
  }

  @JsonCreator
  public Booking(@JsonProperty("account") Account account,
                  @JsonProperty("regularSession") RegularSession regularSession,
                  @JsonProperty("activity") Activity activity,
                  @JsonProperty("participants") Integer participants,
                 @JsonProperty("amount") BigDecimal amount) {
    this.account = account;
    this.regularSession = regularSession;
    this.activity = activity;
    this.participants = participants;
    this.setAmount(amount);
  }

  public Booking(BigDecimal amount, Account account, Integer participants, Activity activity, RegularSession regularSession) {
    this.activity = activity;
    this.participants = participants;
    this.regularSession = regularSession;
    this.account = account;
    this.setAmount(amount);
  }

  public Date getCreatedAt() {
    return created_at;
  }

  public void setCreatedAt(Date created_at) {
    this.created_at = created_at;
  }

  public Date getUpdatedAt() {
    return updated_at;
  }

  public void setUpdatedAt(Date updated_at) {
    this.updated_at = updated_at;
  }

  @JsonIgnoreProperties("bookings")
  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  @JsonIgnoreProperties("bookings")
  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public int getParticipants() {
    return participants;
  }

  public void setParticipants(int participants) {
    this.participants = participants;
  }

  @JsonIgnoreProperties("activities")
  public RegularSession getRegularSession() {
    return regularSession;
  }

  public void setRegularSession(RegularSession regularSession) {
    this.regularSession = regularSession;
  }

  public static Booking createBookingFromRegularSession(Activity activity, Booking booking){
    Integer participants = 1;
    BigDecimal cost = activity.getCost().multiply(new BigDecimal(0.7));
    Account account = booking.getAccount();
    RegularSession regularSession = activity.getRegularSession();
    Booking newBooking = new Booking(cost, account, participants, activity, regularSession);
    return newBooking;
  }
}
