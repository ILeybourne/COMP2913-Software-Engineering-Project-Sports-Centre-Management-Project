package uk.ac.leeds.comp2913.api.ViewModel;

import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class ActivityDTO {
  private Long id;
  private Date endTime;
  private Date startTime;
  private String name;
  private BigDecimal cost;
  private Boolean social;
  private Integer currentCapacity;
//  private Set<Booking> bookings;
//  private Resource resource;

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

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

//  public Set<Booking> getBookings() {
//    return bookings;
//  }
//
//  public void setBookings(Set<Booking> bookings) {
//    this.bookings = bookings;
//  }
//
//  public Resource getResource() {
//    return resource;
//  }
//
//  public void setResource(Resource resource) {
//    this.resource = resource;
//  }
}
