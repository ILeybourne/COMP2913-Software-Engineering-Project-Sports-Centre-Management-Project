package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

//Used by manager to create activities, can pass a regular session boolean to create a regular session
public class ActivityDTO extends RepresentationModel<ActivityDTO> {
  //Post
  @NotNull(message = "start time is required")
  private Date startTime;
  @NotNull(message = "end time is required")
  private Date endTime;

  private boolean regularSession;
  private Boolean social;
  private Integer interval;

  //Others for Get
  private Long id;
  private String name;
  private Resource resource;
  private Date created_at;
  private Date updated_at;
  private Integer currentCapacity;
  private BigDecimal cost;
  private RegularSession regularSessionId;
  private Long activity_type_id;

  public ActivityDTO (){
  }

  @JsonCreator
  public ActivityDTO(@JsonProperty("startTime") Date startTime,
                     @JsonProperty("endTime") Date endTime,
                     @JsonProperty("resourceId") Long resourceId,
                     @JsonProperty(value = "regularSession") Boolean regularSession,
                     @JsonProperty(value = "social") Boolean social,
                     @JsonProperty("interval") Integer interval) {
    if (regularSession == null) {
      regularSession = false;
    }
    if (social == null) {
      social = false;
    }
    if(!regularSession) {
      interval = null;
    }
    this.regularSession = (Boolean) regularSession;
    this.social = (Boolean) social;
    this.startTime = startTime;
    this.endTime = endTime;
    this.interval = interval;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

   @JsonIgnore
  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }


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

  @JsonIgnoreProperties({"activities", "bookings", "createdAt", "updatedAt"})
  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
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

  @JsonProperty
  public Integer getCurrentCapacity() {
    return currentCapacity;
  }

  public void setCurrentCapacity(Integer currentCapacity) {
    this.currentCapacity = currentCapacity;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  @JsonIgnoreProperties({"activities", "bookings"})
  public RegularSession getRegularSessionId() {
    return regularSessionId;
  }

  public void setRegularSessionId(RegularSession regularSessionId) {
    this.regularSessionId = regularSessionId;
  }


  public Boolean isSocial() {
    return social;
  }

  public void setSocial(Boolean social) {
    this.social = social;
  }


  public boolean isRegularSession() {
    return regularSession;
  }

  public void setRegularSession(boolean regularSession) {
    this.regularSession = regularSession;
  }

  public Long getActivity_type_id() {
    return activity_type_id;
  }

  public void setActivity_type_id(Long activity_type_id) {
    this.activity_type_id = activity_type_id;
  }
}