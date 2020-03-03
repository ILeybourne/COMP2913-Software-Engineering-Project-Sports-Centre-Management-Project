package uk.ac.leeds.comp2913.api.Domain.Model;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

//defining an entity
@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    private Date start_time;

    private Date end_time;

    @OneToMany(mappedBy = "activity")
    private List<Booking> bookings;

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

    public Resource getResource() {
        return resource;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
