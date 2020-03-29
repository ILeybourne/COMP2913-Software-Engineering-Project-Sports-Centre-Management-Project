package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class RegularSession {
  public RegularSession(){
  }
  @JsonCreator
  public RegularSession(@JsonProperty("interval") Integer interval,
                        @JsonProperty("activity") Activity activity){
    this.interval = interval;
    activity.setRegularSession(this);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //The activities that are of type regular session
  @OneToMany(mappedBy = "regularSession", fetch = FetchType.EAGER)
  private Set<Activity> activities;

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



  //public void createRecurringActivities(Activity originalActivity, Date startTime, Date endTime, Integer occurrence, Integer frequency){
 //  Integer occurrence = occurrence;
 //  Integer Frequency = frequency;
 //  for (int i = 0; i < occurrence; i++) {
 //    originalActivity = originalActivity;
 //    Date newStartTime = startTime + frequency;
 //    Date newEndTime = endTime + frequency;
 //    frequency = frequency + frequency;
 //    RegularSession regularSession = new RegularSession(originalActivity, newStartTime, newEndTime);
 //  }
 //}
}
