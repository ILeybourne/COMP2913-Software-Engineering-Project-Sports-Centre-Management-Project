package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Any model in our domain that may account to sales should extend this
 * class and it provides a standard, polymorphic interface to
 * treat sales data
 * <p>
 * If there is no receipt associated with this sale, then this hasn't
 * been paid for yet
 * <p>
 * A customer can choose to pay via Cash or via Card and that will be determined by payment type
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Membership.class, name = "membership"),
  @JsonSubTypes.Type(value = Booking.class, name = "booking")
})
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  /**
   * The Receipt in which the payment was sent out on
   * <p>
   * When a Sale is added to a receipt, we deem the sale paid.
   * <p>
   * Hence if we need to get the time of payment, we get the created_at
   * timestamp from the receipt
   */
  @ManyToOne
  @JoinColumn(name = "receipt_id")
  private Receipt receipt;


  /**
   * The Transaction ID verifying the payment
   * <p>
   * In case of card handling system eg Stripe, or Random Transaction ID if cash payment
   */
  @Column(name = "transaction_id", nullable = true, unique = true)
  @Length(max = 255)
  private String transactionId;

  public long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal cost) {
    this.amount = cost;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public Receipt getReceipt() {
    return receipt;
  }

  public void setReceipt(Receipt receipt) {
    this.receipt = receipt;
  }

  public abstract String getName();
}
