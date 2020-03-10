package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Booking {

  @Id
  @GeneratedValue
  private long id;

  @CreationTimestamp
  private Date created_at;
  @UpdateTimestamp
  private Date updated_at;


  @OneToOne
  @JoinColumn(name = "account_id")
  private Account account;
  /**
   * The Activity associated with the booking
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_id")
  private Activity activity;

  public Booking() {
  }

  public long getId() {
    return id;
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

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}
