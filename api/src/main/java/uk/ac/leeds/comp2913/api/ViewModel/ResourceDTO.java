package uk.ac.leeds.comp2913.api.ViewModel;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Set;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Centre;

public class ResourceDTO extends RepresentationModel<ResourceDTO> {
    private Long id;
    private String name;
    private Long centreId;
    private Date createdAt;
    private Date updatedAt;

    public ResourceDTO(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCentreId() {
        return centreId;
    }

    public void setCentreId(Long centreId) {
        this.centreId = centreId;
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



}
