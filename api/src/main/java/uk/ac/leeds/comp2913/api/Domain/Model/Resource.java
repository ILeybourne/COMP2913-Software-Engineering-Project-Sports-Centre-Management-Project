package uk.ac.leeds.comp2913.api.Domain.Model;

import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class Resource {
    @Id
    @GeneratedValue
    private Long id;
    private String name; // Astro Turf

    @OneToMany(mappedBy = "resource")
    private List<Activity> activities;
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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
