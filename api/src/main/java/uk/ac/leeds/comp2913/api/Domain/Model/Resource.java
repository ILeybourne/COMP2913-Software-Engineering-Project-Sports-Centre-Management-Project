package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * TODO: @CHORE Rename this entity to 'resource' as it fits more with spec
 * <p>
 * Represents a resource available inside the sports center. eg Astro Turf, Squash Court, Swimming Pool etc.
 */
@Proxy(lazy = false)
@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "name is required")
    @Size(min = 3, max = 20)
    private String name;

    @Size(max = 255)
    private String description;

    @ManyToOne
    private Centre centre;

    /**
     * List of activities ever booked for the resource
     */
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    private Set<Activity> activities;

    /**
     * List of activities held at the resource
     */
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    private Set<ActivityType> activityTypes;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Length(min = 5, max = 255)
    private String imagePath;

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
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


    @JsonIgnore
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageName() {
        return "resource-" + this.id;
    }

    public void setFilePath(String filename) {
        this.imagePath = filename;
    }

    public String getFilePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
