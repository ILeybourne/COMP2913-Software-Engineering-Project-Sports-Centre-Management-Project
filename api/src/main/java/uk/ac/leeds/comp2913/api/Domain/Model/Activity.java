package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @Column(name = "current_capacity")
    private Integer currentCapacity;

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
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType activityType;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    private BigDecimal cost;

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

    @JsonIgnoreProperties({ "activities", "activityTypes" })
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
}
