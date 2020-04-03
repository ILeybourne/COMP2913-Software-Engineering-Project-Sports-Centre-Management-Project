package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

//Used to deserialize json passed from front end, allows boolean to be passed to create regular bookings
public class BookingDTO {
  @Range(min = 1)
  private Integer participants;
  private boolean regularBooking;

  public BookingDTO() {
  }

  @JsonCreator
  public BookingDTO(@JsonProperty("participants") Integer participants,
                    @JsonProperty("amount") BigDecimal amount,
                    @JsonProperty(value = "regularBooking") Boolean regularBooking) {
    if (regularBooking == null) {
      regularBooking = false;
    }
    this.regularBooking = (boolean) regularBooking;
    this.participants = participants;
  }

  public Integer getParticipants() {
    return participants;
  }

  public void setParticipants(Integer participants) {
    this.participants = participants;
  }

  public boolean isRegularBooking() {
    return regularBooking;
  }

  public void setRegularBooking(boolean regularBooking) {
    this.regularBooking = regularBooking;
  }
}
