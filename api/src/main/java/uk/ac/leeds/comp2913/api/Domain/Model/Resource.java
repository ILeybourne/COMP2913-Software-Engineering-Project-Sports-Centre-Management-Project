package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


/**
 * Represents a resource available inside the sports center. eg Astro Turf, Squash Court, Swimming Pool etc.
 */
@Entity
public class Resource {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Centre centre;

    /**
     * List of activities ever booked for the resource
     */
    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    private Set<Activity> activities;

    /**
     * List of activities held at the resource
     */
    @JsonProperty
    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    private Set<ActivityType> activityTypes;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnoreProperties("activityType")
    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnoreProperties("resource")
    public Set<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(Set<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }
}
