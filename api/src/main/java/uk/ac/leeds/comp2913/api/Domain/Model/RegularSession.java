package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
public class RegularSession {
  public RegularSession(){
  }

  @JsonCreator
  public RegularSession(@JsonProperty("interval") Integer interval,
                        @JsonProperty("activity") Activity activity){
    this.interval = interval;
  //  activity.setRegularSession(this);
  }

  public RegularSession(Activity activity, Integer interval){
   // activity.setRegularSession(this);
    this.interval = interval;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //The activities that are of type regular session
  @OneToMany(mappedBy = "regularSession", fetch = FetchType.EAGER, cascade=ALL, orphanRemoval=true)
  private Set<Activity> activities;

  @OneToMany(mappedBy = "regularSession", fetch = FetchType.EAGER, cascade=ALL, orphanRemoval=true)
  private Set<Booking> bookings;

  @Column(name = "interval")
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

  @JsonIgnoreProperties({"activity", "regularSession"})
  public Set<Booking> getBookings() {
    return bookings;
  }

  public void setBookings(Set<Booking> bookings) {
    this.bookings = bookings;
  }

}
