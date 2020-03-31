package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


/**
 * This class represents scheduled activities available to the members of the sports center that they can book onto
 * <p>
 * These activities can only be created by the staff at the sports center by selecting a timeslot in the timetable
 * and should be displayed on the weekly timetable for booking
 */
@Entity
public class Activity {
  public Activity() {
  }

  @JsonCreator
  public Activity(@JsonProperty("startTime") Date startTime,
                  @JsonProperty("endTimes") Date endTime,
                  @JsonProperty("activityType") ActivityType activityType,
                  @JsonProperty("resource") Resource resource) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.activityType = activityType;
    this.name = activityType.getName();
    this.cost = activityType.getCost();
    this.resource = resource;
  }

  //Constructor for auto posting regular sessions
  public Activity(Date startTime, Date endTime, Activity activity) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.activityType = activity.getActivityType();
    this.name = activity.getName();
    this.cost = activity.getCost();
    this.resource = activity.getResource();
    this.regularSession = activity.getRegularSession();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Social, when true the activity is open for individual members to book onto
   * When false, the activity is private to the booker.
   * For example, squash court has a total capacity of 4, when booking a one hour session and social is true
   * 4 members can book on
   * when false, one member can book the full squash court for the one hour session
   */
  private Boolean social;

  private String name;

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  /**
   * The bookings that have been made against the activity
   */
  @JsonIgnore
  @OneToMany(mappedBy = "activity", fetch = FetchType.EAGER)
  private Set<Booking> bookings;


  /**
   * Which resource the activity needs to take place
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "resource_id", nullable = false)
  private Resource resource;


  /**
   * Which activity type the activity belongs to
   */
  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "activity_type_id", nullable = false)
  private ActivityType activityType;

  @CreationTimestamp
  private Date created_at;
  @UpdateTimestamp
  private Date updated_at;

  @Formula("(SELECT SUM(b.participants) FROM sports_centre_management.booking b where b.activity_id = id)")
  private Integer currentCapacity;

  private BigDecimal cost;

  //IF an activity is a regular session, then it will hold the ID of the regular session
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "regular_session_id", nullable = true)
  private RegularSession  regularSession;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date start_time) {
    this.startTime = start_time;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date end_time) {
    this.endTime = end_time;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty
  public Integer getCurrentCapacity() {
    return currentCapacity;
  }

  public void setCurrentCapacity(Integer current_capacity) {
    this.currentCapacity = current_capacity;
  }

  public Set<Booking> getBookings() {
    return bookings;
  }

  public void setBookings(Set<Booking> bookings) {
    this.bookings = bookings;
  }

  @JsonIgnoreProperties({"activities", "activityTypes"})
  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public Boolean getSocial() {
    return social;
  }

  public void setSocial(Boolean social) {
    this.social = social;
  }

  public ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
  }

  @JsonIgnoreProperties({"activities"})
  public RegularSession getRegularSession() {
    return regularSession;
  }

  public void setRegularSession(RegularSession regularSession) {
    this.regularSession = regularSession;
  }

  public static Activity createFromLastScheduled(Activity last_scheduled) {
    RegularSession regularsession = last_scheduled.getRegularSession();
    Integer interval = regularsession.getInterval();
    //Date start1 = last_scheduled.getStartTime();
    //Date end2 = last_scheduled.getEndTime();
    Date start = addIntervalToDate(last_scheduled.getStartTime(), interval);
    Date end = addIntervalToDate(last_scheduled.getEndTime(), interval);
    Activity activity = new Activity(start, end, last_scheduled);
    return activity;
  }

  public static Date addIntervalToDate(Date date, Integer interval){
    //Date oldDate = date;
    Date newDate = new Date(date.getTime()+((24*60*60*1000) * interval)); //Math at the end converts a day into milliseconds
    return newDate;
  }
}
