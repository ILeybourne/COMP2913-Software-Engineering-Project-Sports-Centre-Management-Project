package uk.ac.leeds.comp2913.api.ViewModel;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class CustomerDTO extends RepresentationModel<CustomerDTO> {
    private Long id;
    private String emailAddress;
    private Date dateOfBirth;

    public CustomerDTO(){}

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
