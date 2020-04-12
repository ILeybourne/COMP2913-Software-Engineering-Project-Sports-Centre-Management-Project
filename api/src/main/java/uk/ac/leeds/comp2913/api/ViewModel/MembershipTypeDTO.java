package uk.ac.leeds.comp2913.api.ViewModel;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Date;

import javax.management.relation.RelationException;

import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;

public class MembershipTypeDTO extends RepresentationModel<MembershipTypeDTO> {
    private long id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private int duration;
    private BigDecimal cost;


    public MembershipTypeDTO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

}
