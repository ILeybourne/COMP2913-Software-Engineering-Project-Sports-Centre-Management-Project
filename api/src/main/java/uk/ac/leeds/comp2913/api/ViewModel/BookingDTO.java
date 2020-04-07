package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//Used to deserialize json passed from front end, allows boolean to be passed to create regular bookings
public class BookingDTO {
  @Range(min = 1)
  private Integer participants;
  private boolean regularBooking;

  @NotNull(message = "Account Id is required")
  private Long accountId;

  public BookingDTO() {
  }

  @JsonCreator
  public BookingDTO(@JsonProperty("participants") Integer participants,
                    @JsonProperty("amount") BigDecimal amount,
                    @JsonProperty("accountId") Long accountId,
                    @JsonProperty(value = "regularBooking") Boolean regularBooking) {
    if (regularBooking == null) {
      regularBooking = false;
    }
    this.regularBooking = (boolean) regularBooking;
    this.participants = participants;
    this.accountId = accountId;
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

  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

}
