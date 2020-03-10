package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Booking {

  @Id
  @GeneratedValue
  private long id;

  @CreationTimestamp
  private Date created_at;
  @UpdateTimestamp
  private Date updated_at;


  /**
   * The account associated with the booking
   */
  @ManyToOne
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

  @JsonIgnoreProperties("bookings")
  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }
}
