package uk.ac.leeds.comp2913.api.Domain.Model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Customer extends User {
  @OneToOne
  @JoinColumn(name = "booking_id")
  private Account Account;
}
