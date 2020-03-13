package uk.ac.leeds.comp2913.api.Domain.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents types of activities available in a resource. to be used by the
 * manager to create activities (activity.java) for the public to book onto
 */

@Entity
public class ActivityType {

    @JsonProperty
    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @CreationTimestamp
    private Date created_at;

    @JsonIgnore
    @UpdateTimestamp
    private Date updated_at;

    private String name;

    private BigDecimal cost;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    /**
     * Which resource the activity needs to take place
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;


    //@Column(name = "current_capacity")
    //private Integer currentCapacity;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "resource_id", nullable = false)
    //private Resource resource;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    @JsonIgnoreProperties({ "activities", "activityTypes" })
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    // public Integer getCurrentCapacity() {
        //return currentCapacity;
   // }

    //public void setCurrentCapacity(Integer currentCapacity) {
       // this.currentCapacity = currentCapacity;
 //   }
}
