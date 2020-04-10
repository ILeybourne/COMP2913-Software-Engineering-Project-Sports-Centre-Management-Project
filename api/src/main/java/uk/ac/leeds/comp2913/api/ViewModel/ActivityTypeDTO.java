package uk.ac.leeds.comp2913.api.ViewModel;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Date;

public class ActivityTypeDTO extends RepresentationModel<ActivityTypeDTO> {
    private long id;
    private Date created_at;
    private Date updated_at;
    private String name;
    private BigDecimal cost;

    public ActivityTypeDTO(){}


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

    private Integer totalCapacity;
}