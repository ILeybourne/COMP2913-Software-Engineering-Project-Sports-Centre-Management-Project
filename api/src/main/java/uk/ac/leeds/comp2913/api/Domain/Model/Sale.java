package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

import javax.persistence.*;

/**
 * Any model in our domain that may account to sales should extend this
 * class and it provides a standard, polymorphic interface to
 * treat sales data
 *
 * If there is no payment associated with this sale, then this hasn't
 * been paid for
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Membership.class, name = "membership"),
  @JsonSubTypes.Type(value = Booking.class, name = "booking")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Sale {
  @Id
  @GeneratedValue
  private long id;

  @Column(name = "cost")
  private BigDecimal cost;

  @OneToOne
  @JoinColumn(name = "payment_id")
  private Payment payment;

  public long getId() {
    return id;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }
}
