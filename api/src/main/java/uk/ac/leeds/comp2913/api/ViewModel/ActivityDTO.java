package uk.ac.leeds.comp2913.api.ViewModel;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class ActivityDTO {
  private Long id;
  private Date endTime;
  private Date startTime;
  private Date created_at;

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

  private Date updated_at;
  private String name;
  private BigDecimal cost;
  private Boolean social;
  private Integer currentCapacity;
  private ActivityType activityType;
//  private Set<Booking> bookings;
  private Resource resource;
  public ActivityDTO(){

  }

//public ActivityDTO (Long id, Date endTime,Date created_at, Date updated_at, Date startTime, String name, BigDecimal cost, Boolean social, Integer currentCapacity, ActivityType activityType, Resource resource) {
//  this.id = id;
//  this.endtime = endTime;
//  this.startTime = startTime;
//  this.created_at = created_at;
//  this.updated_at = updated_at;
//  this.name = name;
//  this.cost = cost;
//  this.social = social;
//  this.currentCapacity = currentCapacity;
//  this.activityType = activityType;
//  this.resource = resource;
//}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getSocial() {
    return social;
  }

  public void setSocial(Boolean social) {
    this.social = social;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCurrentCapacity() {
    return currentCapacity;
  }

  public void setCurrentCapacity(Integer currentCapacity) {
    this.currentCapacity = currentCapacity;
  }

  public Date getStart_time() {
    return startTime;
  }

  public void setStart_time(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEnd_time() {
    return endTime;
  }

  public void setEnd_time(Date endTime) {
    this.endTime = endTime;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public ActivityType getActivity_type_id() {
    return activityType;
  }

  public void setActivity_type_id(ActivityType activityType) {
    this.activityType = activityType;
  }

//  public Set<Booking> getBookings() {
//    return bookings;
//  }
//
//  public void setBookings(Set<Booking> bookings) {
//    this.bookings = bookings;
//  }
//
  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }
}
