package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

//Membership data, including account number & membership type chosen
//Start date and end date (based on duration of chosen membership)
@Entity
public class Membership {

    @Id
    @GeneratedValue
    private long id;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    //Foreign key, membership type (annual, monthly etc)
    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "membership_type_id")
    private MembershipType membershipType;

    //foreign key, account id to link user
    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


    private Date StartDate;
    private Date EndDate;

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    public void makePayment() {
    }

    public void emailReceipt() {
    }

    public void printReceipt() {
    }

    public void storeReceipt() {
    }

    public Account getAccount(Long account_id) {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }


    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void getMembershipType(Long membership_type_id) {
    }
}
