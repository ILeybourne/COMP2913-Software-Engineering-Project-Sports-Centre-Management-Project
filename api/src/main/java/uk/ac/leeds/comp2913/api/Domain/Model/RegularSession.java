package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.PagedModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.Set;

/**
 * Regular sessions are activities that repeat, and allow bookings to be made against them at a reduced price.
 * Allows the activities to be scheduled automatically as well as placing bookings and payments automatically
 */
@Entity
public class RegularSession {
  public RegularSession(){
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //Activities that are regular sessions
  @OneToMany(mappedBy = "regularSession", fetch = FetchType.LAZY)
  private Set<Activity> activities;

  //Regular session bookings
  @OneToMany(mappedBy = "regularSession", fetch = FetchType.LAZY)
  private Set<Booking> bookings;

  //Measured in days (an interval of 7 is weekly activities)
  @Range(min = 1)
  @NotNull
  @Column(name = "interval", nullable = false)
  private Integer interval;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Activity> getActivities() {
    return activities;
  }

  public void setActivities(Set<Activity> activities) {
    this.activities = activities;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  @JsonIgnore
  public Set<Booking> getBookings() {
    return bookings;
  }

  public void setBookings(Set<Booking> bookings) {
    this.bookings = bookings;
  }
}
