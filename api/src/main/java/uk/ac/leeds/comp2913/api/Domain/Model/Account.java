package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    @ManyToOne
    private Centre centre;

    @OneToOne(mappedBy = "account",fetch = FetchType.EAGER)
    private Membership Memberships;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "account")
    private List<Booking> bookings;

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }


    @JsonIgnore
    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    @JsonIgnore
    public Membership getMemberships() {
        return Memberships;
    }

    public void setMemberships(Membership memberships) {
        Memberships = memberships;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void buyMembership() {
    }

    public void cancelMembership() {
    }

    public List<Booking> getBookings() {
      return bookings;
    }

    public void setBookings(List<Booking> bookings) {
      this.bookings = bookings;
    }
}
