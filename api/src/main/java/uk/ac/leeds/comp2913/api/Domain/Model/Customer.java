package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer extends RepresentationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Receipt> receipts = new LinkedList<>();

    @OneToMany(mappedBy = "customer")
    private List<Account> Account;

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

    @JsonIgnore
    public List<uk.ac.leeds.comp2913.api.Domain.Model.Account> getAccount() {
        return Account;
    }

    public void setAccount(List<uk.ac.leeds.comp2913.api.Domain.Model.Account> account) {
        Account = account;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void addReceipt(Receipt receipt) {
        this.getReceipts().add(receipt);
        receipt.setCustomer(this);
    }
}
