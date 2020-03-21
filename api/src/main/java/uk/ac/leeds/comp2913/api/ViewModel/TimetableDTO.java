package uk.ac.leeds.comp2913.api.ViewModel;

import java.util.Collection;
import java.util.Date;

public class TimetableDTO {
  private String name;
  private Date startTime;
  private Date endTime;
  private Collection<BookingDTO> bookings;
  private ResourceDTO resource;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public Collection<BookingDTO> getBookings() {
    return bookings;
  }

  public void setBookings(Collection<BookingDTO> bookings) {
    this.bookings = bookings;
  }

  public ResourceDTO getResource() {
    return resource;
  }

  public void setResource(ResourceDTO resource) {
    this.resource = resource;
  }
}
