package uk.ac.leeds.comp2913.api.Domain.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Customer extends User {

    @OneToOne(mappedBy = "customer")
    private Account Account;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
