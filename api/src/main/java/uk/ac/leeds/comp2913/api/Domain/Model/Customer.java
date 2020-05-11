package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "email is required")
    @Size(min = 3, max = 50)
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Receipt> receipts = new LinkedList<>();

    @OneToMany(mappedBy = "customer")
    private List<Account> Account;

    @NotNull(message = "date of birth required")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "auth0_username")
    private String auth0_username;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    private String stripeId;

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    @JsonIgnore
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuth0_username() {
        return auth0_username;
    }

    public void setAuth0_username(String auth0_username) {
        this.auth0_username = auth0_username;
    }
}
