package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.CollectionModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;

/**
 * TODO: @CHORE Rename this entity to 'session' as it fits more with spec
 * TODO: @CHORE Add JSR303 validations to the model, check the constraints match the database
 * TODO: @CHORE Fix eager loading and JSON serialisation
 * <p>
 * This class represents scheduled activities available to the members of the sports center that they can book onto
 * <p>
 * These activities can only be created by the staff at the sports center by selecting a timeslot in the timetable
 * and should be displayed on the weekly timetable for booking
 */
@Entity
public class Activity extends CollectionModel<Activity> {

  public Activity() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Social, when true the activity is open for individual members to book onto
   * When false, the activity is private to the booker (like booking out a full squash court)
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


  //Method used within the automatic posting of regular sessions
  public static Activity createFromLastScheduled(Activity last_scheduled) {
    Activity activity = new Activity();
    Integer interval = last_scheduled.getRegularSession().getInterval();
    activity.setRegularSession(last_scheduled.getRegularSession());
    activity.setActivityType(last_scheduled.getActivityType());
    activity.setStartTime(addIntervalToDate(last_scheduled.getStartTime(), interval));
    activity.setEndTime(addIntervalToDate(last_scheduled.getEndTime(), interval));
    activity.setCost(last_scheduled.getCost());
    activity.setResource(last_scheduled.getResource());
    return activity;
  }

  public static Date addIntervalToDate(Date date, Integer interval){
    Date newDate = new Date(date.getTime()+((24*60*60*1000) * interval)); //Math at the end converts a day into milliseconds
    return newDate;
  }

  //Clones data from activity_type to this activity
  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
    this.name = activityType.getName();
    this.resource = activityType.getResource();
    this.cost = activityType.getCost();
  }

  public ActivityType getActivityType() {
    return activityType;
  }

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

  @JsonIgnoreProperties({"activities", "bookings"})
  public RegularSession getRegularSession() {
    return regularSession;
  }

  public void setRegularSession(RegularSession regularSession) {
    this.regularSession = regularSession;
  }

}
