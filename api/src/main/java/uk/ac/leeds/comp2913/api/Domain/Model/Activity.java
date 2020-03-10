package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

import javax.persistence.*;


/**
 * This class represents an activity available to the members of the sports center that they can book onto
 *
 * These activities can only be created by the staff at the sports center and should be displayed on the weekly
 * timetable
 */
@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "current_capacity")
    private Integer currentCapacity;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    /**
     * The bookings that have been made against the activity
     */
    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private List<Booking> bookings;


  /**
   * Which resource the activity needs to take place
   */
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

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

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer total_capacity) {
        this.totalCapacity = total_capacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer current_capacity) {
        this.currentCapacity = current_capacity;
    }

    public void getResource(Long resource_id) {
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @JsonIgnoreProperties("activities")
    public Resource getResource() {
      return resource;
    }

    public void setResource(Resource resource) {
      this.resource = resource;
    }

}
