package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;


/**
 * Represents a resource available inside the sports center. eg Astro Turf, Squash Court, Swimming Pool etc.
 */
@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

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


    public void setId(Long id) {
        this.id = id;
    }


    @JsonIgnoreProperties("resource")
    public Set<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(Set<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public void addActivityType(ActivityType type) {
      activityTypes.add(type);
      type.setResource(this);
    }

    public void removeActivityType(ActivityType type) {
      activityTypes.remove(type);
      type.setResource(null);
    }
}
