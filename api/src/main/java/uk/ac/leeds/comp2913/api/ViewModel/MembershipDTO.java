package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MembershipDTO extends RepresentationModel<MembershipDTO> {

    @NotEmpty(message = "email is required")
    @Size(min = 3, max = 20)
    private String emailAddress;
    @NotNull
    private Date dateOfBirth;
    private boolean repeatingPayment;
    private Date created_at;
    private Date updated_at;
    private Date startDate;
    private Date endDate;
    private String name;
    private Long id;

    public MembershipDTO(){}

    @JsonCreator
    public MembershipDTO(@JsonProperty("email") String emailAddress,
                         @JsonProperty("dateOfBirth") Date dateOfBirth,
                         @JsonProperty(value = "repeatingPayment") Boolean repeatingPayment){
        if (repeatingPayment == null) {
            repeatingPayment = false;
        }
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.repeatingPayment = (boolean) repeatingPayment;
    }

    public boolean isRepeatingPayment() {
        return repeatingPayment;
    }

    public void setRepeatingPayment(boolean repeatingPayment) {
        this.repeatingPayment = repeatingPayment;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonIgnore
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
