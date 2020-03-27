package uk.ac.leeds.comp2913.api.Domain.Model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private List<Receipt> receipts;

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

  public List<Receipt> getReceipts() {
    return receipts;
  }

  public void setReceipts(List<Receipt> receipts) {
    this.receipts = receipts;
  }
}
