package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


//defining an entity
@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer total_capacity;

    private Integer current_capacity;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Date start_time;

    private Date end_time;

//    @OneToMany(mappedBy = "activity")
//    private List<Booking> bookings;

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
        return start_time;
    }

    public void setStartTime(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEndTime() {
        return end_time;
    }

    public void setEndTime(Date end_time) {
        this.end_time = end_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Booking getBooking() {
        return booking;
    }

    public Integer getTotalCapacity() {
        return total_capacity;
    }

    public void setTotalCapacity(Integer total_capacity) {
        this.total_capacity = total_capacity;
    }

    public Integer getCurrentCapacity() {
        return current_capacity;
    }

    public void setCurrentCapacity(Integer current_capacity) {
        this.current_capacity = current_capacity;
    }

    public void getResource(Long resource_id) {
    }

    @JsonIgnoreProperties("activities")
    public Resource getResource() {
      return resource;
    }

    public void setResource(Resource resource) {
      this.resource = resource;
    }

}
