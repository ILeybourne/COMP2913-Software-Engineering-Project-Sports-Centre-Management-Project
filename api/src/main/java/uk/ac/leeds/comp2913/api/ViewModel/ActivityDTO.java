package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

//Used by manager to create activities, can pass a regular session boolean to create a regular session
public class ActivityDTO {
  @NotBlank(message = "start time is required")
  private Date startTime;

  @NotBlank(message = "end time is required")
  private Date endTime;
  private boolean regularSession;
  private boolean social;
  private Integer interval;

  @NotNull(message = "activity type is required")
  private Long activityTypeId;

  public ActivityDTO (){
  }

  @JsonCreator
  public ActivityDTO(@JsonProperty("startTime") Date startTime,
                     @JsonProperty("endTime") Date endTime,
                     @JsonProperty(value = "RegularSession") Boolean regularSession,
                     @JsonProperty(value = "social") Boolean social,
                     @JsonProperty("interval") Integer interval,
                     @JsonProperty("activityTypeId") Long activityTypeId) {
    if (regularSession == null) {
      regularSession = false;
    }
    if (social == null) {
      social = false;
    }
    this.regularSession = (boolean) regularSession;
    this.social = (boolean) social;
    this.startTime = startTime;
    this.endTime = endTime;
    this.interval = interval;
    this.activityTypeId = activityTypeId;
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

  public boolean isRegularSession() {
    return regularSession;
  }

  public void setRegularSession(boolean regularSession) {
    this.regularSession = regularSession;
  }

  public boolean isSocial() {
    return social;
  }

  public void setSocial(boolean social) {
    this.social = social;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  public Long getActivityTypeId() {
    return activityTypeId;
  }

  public void setActivityTypeId(Long activityTypeId) {
    this.activityTypeId = activityTypeId;
  }
}