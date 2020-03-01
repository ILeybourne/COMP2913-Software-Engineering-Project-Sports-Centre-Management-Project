package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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

    @ManyToOne
    @JoinColumn(name="resource_id", nullable=false)
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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
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

    public Integer getTotal_capacity() {
        return total_capacity;
    }

    public void setTotal_capacity(Integer total_capacity) {
        this.total_capacity = total_capacity;
    }

    public Integer getCurrent_capacity() {
        return current_capacity;
    }

    public void setCurrent_capacity(Integer current_capacity) {
        this.current_capacity = current_capacity;
    }

    public void getResource(Long resource_id) {
    }
}
