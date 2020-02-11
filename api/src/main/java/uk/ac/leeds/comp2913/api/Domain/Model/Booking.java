package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
}
